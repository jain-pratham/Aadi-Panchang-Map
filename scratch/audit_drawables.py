import os

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"
app_drawable = os.path.join(repo_root, "app", "src", "main", "res")

drawables = []
for root, dirs, files in os.walk(app_drawable):
    if "drawable" in os.path.basename(root):
        for f in files:
            full_p = os.path.join(root, f)
            rel_p = os.path.relpath(full_p, repo_root)
            size = os.path.getsize(full_p)
            drawables.append((rel_p, f, size))

print(f"Total drawables in app module: {len(drawables)}")
for rel_p, f, size in sorted(drawables):
    print(f"DRAWABLE: {rel_p} ({size} bytes)")
