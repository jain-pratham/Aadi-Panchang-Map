import os
import re

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"
app_res = os.path.join(repo_root, "app", "src", "main", "res")

credits_files = []
for root, dirs, files in os.walk(app_res):
    if "credits.xml" in files:
        fp = os.path.join(root, "credits.xml")
        rel_p = os.path.relpath(fp, repo_root)
        credits_files.append((fp, rel_p))

print(f"Total credits.xml files found: {len(credits_files)}\n")

for fp, rel_p in credits_files:
    with open(fp, "r", encoding="utf-8", errors="ignore") as f:
        content = f.read()
    matches = re.findall(r'(&lt;p&gt;.*?Google.*?&lt;/p&gt;|<p>.*?Google.*?</p>)', content, flags=re.IGNORECASE)
    for m in matches:
        clean_m = m.encode('ascii', 'replace').decode('ascii')
        print(f"{rel_p:40s} | {clean_m}")
