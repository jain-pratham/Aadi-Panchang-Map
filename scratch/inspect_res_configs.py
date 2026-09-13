import os

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"
app_res = os.path.join(repo_root, "app", "src", "main", "res")

print("=== MANIFEST LAUNCHER CONFIG ===")
manifest_p = os.path.join(repo_root, "app", "src", "main", "AndroidManifest.xml")
with open(manifest_p, "r", encoding="utf-8", errors="ignore") as f:
    for line_num, line in enumerate(f, 1):
        if any(k in line for k in ["android:icon", "android:roundIcon", "android:logo", "theme"]):
            print(f"L{line_num}: {line.strip()}")

print("\n=== SPLASH LAYOUT (splash.xml) ===")
splash_p = os.path.join(app_res, "layout", "splash.xml")
if os.path.exists(splash_p):
    with open(splash_p, "r", encoding="utf-8", errors="ignore") as f:
        print(f.read())

print("\n=== MIPMAP DIRECTORY CONTENTS ===")
for root, dirs, files in os.walk(app_res):
    if "mipmap" in os.path.basename(root):
        rel_dir = os.path.basename(root)
        print(f"\n--- {rel_dir} ---")
        for f in files:
            full_p = os.path.join(root, f)
            size = os.path.getsize(full_p)
            print(f"  {f:40s} ({size} bytes)")
