import os
import re

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"

# 1. Remove video.txt
video_txt = os.path.join(repo_root, "fastlane", "metadata", "android", "en-US", "video.txt")
if os.path.exists(video_txt):
    os.remove(video_txt)
    print(f"REMOVED: {video_txt}")

# 2. Update localized titles in specified locales
title_locales = ["ur", "zh-HK", "zh-TW", "zu"]
for loc in title_locales:
    title_p = os.path.join(repo_root, "fastlane", "metadata", "android", loc, "title.txt")
    if os.path.exists(title_p):
        with open(title_p, "w", encoding="utf-8") as f:
            f.write("Aadi Panchang Map\n")
        print(f"UPDATED TITLE [{loc}]: Aadi Panchang Map")

# 3. Clean full_description.txt across all locales
fastlane_dir = os.path.join(repo_root, "fastlane", "metadata", "android")
if os.path.exists(fastlane_dir):
    for root, dirs, files in os.walk(fastlane_dir):
        for f in files:
            if f == "full_description.txt":
                fp = os.path.join(root, f)
                with open(fp, "r", encoding="utf-8", errors="ignore") as file:
                    lines = file.readlines()
                new_lines = []
                for line in lines:
                    line_lower = line.lower()
                    # Skip lines containing upstream promotional links or claims
                    if any(k in line_lower for k in [
                        "stardroid.app", "github.com/sky-map-team", "facebook.com/groups/stardroidapp",
                        "x.com/skymapdevs", "twitter.com/skymapdevs", "@skymapdevs", "buymeacoffee.com",
                        "originally developed as google"
                    ]):
                        continue
                    if "find us online:" in line_lower or "support the project" in line_lower:
                        continue
                    new_lines.append(line)
                clean_text = "".join(new_lines).strip() + "\n"
                with open(fp, "w", encoding="utf-8") as file:
                    file.write(clean_text)
                rel_p = os.path.relpath(fp, repo_root)
                print(f"CLEANED STORE DESCRIPTION: {rel_p}")

# 4. Clean assets/playstore_full_description-en_US.html
html_desc = os.path.join(repo_root, "assets", "playstore_full_description-en_US.html")
if os.path.exists(html_desc):
    with open(html_desc, "r", encoding="utf-8", errors="ignore") as file:
        lines = file.readlines()
    new_lines = []
    for line in lines:
        line_lower = line.lower()
        if any(k in line_lower for k in [
            "stardroid.app", "github.com/sky-map-team", "facebook.com/groups",
            "twitter.com/skymapdevs", "x.com/skymapdevs", "groups.google.com/group/google-sky-map",
            "buymeacoffee.com", "originally developed as google"
        ]):
            continue
        if "find us elsewhere:" in line_lower:
            continue
        new_lines.append(line)
    clean_text = "".join(new_lines).strip() + "\n"
    with open(html_desc, "w", encoding="utf-8") as file:
        file.write(clean_text)
    print(f"CLEANED HTML DESCRIPTION: {html_desc}")

# 5. Clean credits.xml in res/values/ and res/values-*/
app_res = os.path.join(repo_root, "app", "src", "main", "res")
for root, dirs, files in os.walk(app_res):
    if "credits.xml" in files:
        fp = os.path.join(root, "credits.xml")
        with open(fp, "r", encoding="utf-8", errors="ignore") as file:
            content = file.read()
        
        # Remove the <h1/h2/p> donation block up to <h1>Credits</h1> or <h2>Credits</h2>
        # Regex to remove donation section
        content_cleaned = re.sub(
            r'(&lt;h1&gt;Support Development&lt;/h1&gt;.*?&lt;h1&gt;Credits&lt;/h1&gt;)',
            r'&lt;h1&gt;Credits&lt;/h1&gt;',
            content,
            flags=re.DOTALL
        )
        content_cleaned = re.sub(
            r'(<h1>Support Development</h1>.*?<h1>Credits</h1>)',
            r'<h1>Credits</h1>',
            content_cleaned,
            flags=re.DOTALL
        )
        content_cleaned = re.sub(
            r'(&lt;a href="https://buymeacoffee.com/skymapdevs"&gt;.*?&lt;/a&gt;)',
            r'',
            content_cleaned
        )
        
        with open(fp, "w", encoding="utf-8") as file:
            file.write(content_cleaned)
        rel_p = os.path.relpath(fp, repo_root)
        print(f"CLEANED CREDITS.XML: {rel_p}")

# 6. Clean help.xml in res/values/ and res/values-*/
for root, dirs, files in os.walk(app_res):
    if "help.xml" in files:
        fp = os.path.join(root, "help.xml")
        with open(fp, "r", encoding="utf-8", errors="ignore") as file:
            content = file.read()
        
        # Replace stardroid.app URL line
        content_cleaned = re.sub(r'&lt;p class="url"&gt;&lt;a href="https://stardroid.app"&gt;https://stardroid.app&lt;/a&gt;&lt;/p&gt;', '', content)
        content_cleaned = re.sub(r'https://stardroid.app', '', content_cleaned)
        
        # Replace skymapdevs@gmail.com with jainpratham4050@gmail.com
        content_cleaned = content_cleaned.replace("skymapdevs@gmail.com", "jainpratham4050@gmail.com")
        
        # Replace upstream github troubleshooting link
        content_cleaned = re.sub(r'https://github.com/sky-map-team/stardroid/blob/master/troubleshooting.md', '', content_cleaned)
        
        with open(fp, "w", encoding="utf-8") as file:
            file.write(content_cleaned)
        rel_p = os.path.relpath(fp, repo_root)
        print(f"CLEANED HELP.XML: {rel_p}")

# 7. Clean whatsnew.xml & changelogs/default.txt
for root, dirs, files in os.walk(app_res):
    if "whatsnew.xml" in files:
        fp = os.path.join(root, "whatsnew.xml")
        with open(fp, "r", encoding="utf-8", errors="ignore") as file:
            content = file.read()
        
        # Remove support/donation headers in whatsnew
        content_cleaned = re.sub(r'(&lt;h1&gt;Support Aadi Panchang Map&lt;/h1&gt;.*?&lt;/p&gt;)', '', content, flags=re.DOTALL)
        content_cleaned = re.sub(r'(&lt;h1&gt;Apoie o Aadi Panchang Map&lt;/h1&gt;.*?&lt;/p&gt;)', '', content_cleaned, flags=re.DOTALL)
        content_cleaned = re.sub(r'(&lt;h1&gt;Podporte Aadi Panchang Map&lt;/h1&gt;.*?&lt;/p&gt;)', '', content_cleaned, flags=re.DOTALL)
        content_cleaned = re.sub(r'(&lt;h1&gt;Podprite Aadi Panchang Map&lt;/h1&gt;.*?&lt;/p&gt;)', '', content_cleaned, flags=re.DOTALL)
        content_cleaned = re.sub(r'(&lt;h1&gt;St?d Aadi Panchang Map&lt;/h1&gt;.*?&lt;/p&gt;)', '', content_cleaned, flags=re.DOTALL)
        content_cleaned = re.sub(r'https://buymeacoffee.com/skymapdevs', '', content_cleaned)
        content_cleaned = re.sub(r'https://github.com/sky-map-team/stardroid\S*', '', content_cleaned)
        
        with open(fp, "w", encoding="utf-8") as file:
            file.write(content_cleaned)
        rel_p = os.path.relpath(fp, repo_root)
        print(f"CLEANED WHATSNEW.XML: {rel_p}")

print("\nTask 4 Cleanup completed successfully!")
