import os
import time

now = time.time()
two_hours_ago = now - 7200

search_paths = [
    r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1",
    r"C:\Users\jainp\.gemini",
    r"C:\Users\jainp\AppData\Local\Temp"
]

print("Searching for recently created PNG files...")
for sp in search_paths:
    if not os.path.exists(sp):
        continue
    for root, dirs, files in os.walk(sp):
        for f in files:
            if f.lower().endswith(('.png', '.jpg', '.jpeg', '.webp')):
                full_p = os.path.join(root, f)
                try:
                    mtime = os.path.getmtime(full_p)
                    if mtime > two_hours_ago:
                        size = os.path.getsize(full_p)
                        print(f"FOUND: {full_p} | Size: {size} | Time: {time.ctime(mtime)}")
                except Exception:
                    pass
