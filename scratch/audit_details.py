import os
import glob
import re

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"

print("==========================================")
print("1. LAUNCHER ICONS IN APP MODULE")
print("==========================================")
app_res = os.path.join(repo_root, "app", "src", "main", "res")
for root, dirs, files in os.walk(app_res):
    for f in files:
        if "ic_launcher" in f or "icon" in f:
            full_p = os.path.join(root, f)
            rel_p = os.path.relpath(full_p, repo_root)
            size = os.path.getsize(full_p)
            print(f"ICON: {rel_p} ({size} bytes)")

print("\n==========================================")
print("2. LOGOS AND SPLASH ARTWORK IN APP MODULE")
print("==========================================")
for root, dirs, files in os.walk(app_res):
    for f in files:
        if any(k in f.lower() for k in ["logo", "splash", "stardroid_big_image", "skymap_logo"]):
            full_p = os.path.join(root, f)
            rel_p = os.path.relpath(full_p, repo_root)
            size = os.path.getsize(full_p)
            print(f"SPLASH/LOGO: {rel_p} ({size} bytes)")

print("\n==========================================")
print("3. AUDIO / VIDEO ASSETS")
print("==========================================")
for root, dirs, files in os.walk(repo_root):
    if ".git" in root or "build" in root: continue
    for f in files:
        if f.endswith(('.mp3', '.wav', '.ogg', '.mp4', '.webm')):
            full_p = os.path.join(root, f)
            rel_p = os.path.relpath(full_p, repo_root)
            print(f"AUDIO/VIDEO: {rel_p}")

print("\n==========================================")
print("4. FONTS SEARCH")
print("==========================================")
for root, dirs, files in os.walk(repo_root):
    if ".git" in root or "build" in root: continue
    for f in files:
        if f.endswith(('.ttf', '.otf', '.ttc', '.woff', '.woff2')):
            full_p = os.path.join(root, f)
            rel_p = os.path.relpath(full_p, repo_root)
            print(f"FONT: {rel_p}")

print("\n==========================================")
print("5. STRINGS.XML AUDIT FOR BRANDING")
print("==========================================")
values_dir = os.path.join(app_res, "values")
if os.path.exists(values_dir):
    for f in os.listdir(values_dir):
        if f.endswith(".xml"):
            full_p = os.path.join(values_dir, f)
            with open(full_p, "r", encoding="utf-8", errors="ignore") as file:
                content = file.read()
                matches = re.findall(r'<string name="([^"]+)">([^<]+)</string>', content)
                for name, val in matches:
                    if any(b in val.lower() for b in ["sky map", "skymap", "stardroid", "penterakt", "google"]):
                        print(f"STRING [{f}]: name='{name}' -> val='{val}'")

print("\n==========================================")
print("6. MANIFEST BRANDING ATTRIBUTES")
print("==========================================")
manifest_p = os.path.join(repo_root, "app", "src", "main", "AndroidManifest.xml")
with open(manifest_p, "r", encoding="utf-8", errors="ignore") as f:
    for line_num, line in enumerate(f, 1):
        if any(k in line for k in ["android:label", "android:icon", "android:roundIcon", "android:logo", "theme"]):
            print(f"MANIFEST L{line_num}: {line.strip()}")
