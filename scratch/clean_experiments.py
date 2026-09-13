import os

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"
exp_dir = os.path.join(repo_root, "fastlane", "experiments")

if os.path.exists(exp_dir):
    for root, dirs, files in os.walk(exp_dir):
        for f in files:
            fp = os.path.join(root, f)
            with open(fp, "r", encoding="utf-8", errors="ignore") as file:
                lines = file.readlines()
            new_lines = []
            for line in lines:
                line_lower = line.lower()
                if any(k in line_lower for k in ["stardroid.app", "@skymapdevs", "skymapdevs@gmail.com", "buymeacoffee.com"]):
                    continue
                new_lines.append(line)
            clean_text = "".join(new_lines)
            with open(fp, "w", encoding="utf-8") as file:
                file.write(clean_text)
            print(f"Cleaned experiment file: {fp}")
