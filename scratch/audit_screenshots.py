import os

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"
fastlane_root = os.path.join(repo_root, "fastlane", "metadata", "android")

screenshots = []
if os.path.exists(fastlane_root):
    for root, dirs, files in os.walk(fastlane_root):
        if "screenshots" in root.lower() or "images" in root.lower():
            for f in files:
                if f.endswith(('.png', '.jpg', '.jpeg', '.webp')):
                    fp = os.path.join(root, f)
                    rel_p = os.path.relpath(fp, repo_root)
                    size = os.path.getsize(fp)
                    screenshots.append((rel_p, f, size))

print(f"Total store screenshot/image assets: {len(screenshots)}\n")
for rel_p, f, size in sorted(screenshots):
    print(f"SCREENSHOT/IMAGE: {rel_p:75s} ({size} bytes)")
