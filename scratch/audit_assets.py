import os
import re

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"

asset_extensions = {
    '.png', '.jpg', '.jpeg', '.webp', '.gif', '.svg', '.9.png',
    '.avif', '.bmp', '.ico', '.tiff', '.tif',
    '.mp3', '.wav', '.ogg', '.mp4', '.webm',
    '.json', '.lottie',
    '.ttf', '.otf', '.ttc', '.woff', '.woff2'
}

found_assets = []

for root, dirs, files in os.walk(repo_root):
    # skip .git, .gradle, build, .idea
    if '.git' in root or '.gradle' in root or '\\build' in root or '.idea' in root:
        continue
    for f in files:
        ext = os.path.splitext(f)[1].lower()
        if f.endswith('.9.png'):
            ext = '.9.png'
        if ext in asset_extensions:
            rel_path = os.path.relpath(os.path.join(root, f), repo_root)
            found_assets.append((rel_path, ext))

print(f"Total media/font/asset files found: {len(found_assets)}")
for path, ext in sorted(found_assets):
    print(f"ASSET: {ext:8s} {path}")
