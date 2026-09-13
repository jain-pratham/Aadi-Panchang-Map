import os

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"
fastlane_root = os.path.join(repo_root, "fastlane", "metadata", "android")

video_files = []
if os.path.exists(fastlane_root):
    for root, dirs, files in os.walk(fastlane_root):
        for f in files:
            if "video" in f.lower():
                fp = os.path.join(root, f)
                rel_p = os.path.relpath(fp, repo_root)
                video_files.append(rel_p)

print(f"Total video.txt files found: {len(video_files)}")
for vf in video_files:
    print(f"VIDEO FILE: {vf}")
