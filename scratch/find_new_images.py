import os
import time

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"

print("Searching for PNG/JPG/WebP files in project...")
for root, dirs, files in os.walk(repo_root):
    if ".git" in root or ".gradle" in root or "build" in root:
        continue
    for f in files:
        if f.lower().endswith(('.png', '.jpg', '.jpeg', '.webp')):
            full_p = os.path.join(root, f)
            rel_p = os.path.relpath(full_p, repo_root)
            mtime = os.path.getmtime(full_p)
            size = os.path.getsize(full_p)
            print(f"{rel_p:70s} | Size: {size:10d} bytes | Modified: {time.ctime(mtime)}")
