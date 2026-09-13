import os
import re
import xml.etree.ElementTree as ET

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"
app_res = os.path.join(repo_root, "app", "src", "main", "res")

keywords = ["sky map", "skymap", "stardroid", "penterakt", "google"]

print("==========================================")
print("STRINGS & RESOURCES BRANDING AUDIT")
print("==========================================")

for root, dirs, files in os.walk(app_res):
    if "values" in os.path.basename(root):
        for f in files:
            if f.endswith(".xml"):
                full_p = os.path.join(root, f)
                rel_p = os.path.relpath(full_p, repo_root)
                try:
                    tree = ET.parse(full_p)
                    for elem in tree.getroot():
                        tag = elem.tag
                        name = elem.attrib.get("name", "")
                        text = "".join(elem.itertext())
                        if any(k in text.lower() for k in keywords):
                            print(f"FILE: {rel_p} | TAG: <{tag} name=\"{name}\"> -> Text: \"{text.strip()[:100]}\"")
                except Exception as e:
                    # fallback regex if xml parse fails
                    with open(full_p, "r", encoding="utf-8", errors="ignore") as fp:
                        content = fp.read()
                        for kw in keywords:
                            if kw in content.lower():
                                print(f"FILE (regex): {rel_p} contains keyword '{kw}'")
