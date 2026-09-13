import os

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"
assets_path = os.path.join(repo_root, "app", "src", "main", "assets")

celestial_files = []
for root, dirs, files in os.walk(assets_path):
    for f in files:
        full_p = os.path.join(root, f)
        rel_p = os.path.relpath(full_p, repo_root)
        size = os.path.getsize(full_p)
        celestial_files.append((rel_p, size))

print(f"Total files in app/src/main/assets: {len(celestial_files)}")
for rel_p, size in sorted(celestial_files):
    print(f"ASSET: {rel_p} ({size} bytes)")
