import os

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"
fastlane_root = os.path.join(repo_root, "fastlane", "metadata", "android")

print("=== 1. FASTLANE LOCALES AND METADATA FILES ===")
if os.path.exists(fastlane_root):
    for item in os.listdir(fastlane_root):
        loc_dir = os.path.join(fastlane_root, item)
        if os.path.isdir(loc_dir):
            print(f"\nLOCALE: {item}")
            for root, dirs, files in os.walk(loc_dir):
                for f in files:
                    fp = os.path.join(root, f)
                    rel_p = os.path.relpath(fp, repo_root)
                    size = os.path.getsize(fp)
                    print(f"  {rel_p} ({size} bytes)")
                    if f.endswith(".txt"):
                        with open(fp, "r", encoding="utf-8", errors="ignore") as txt_f:
                            content = txt_f.read().strip()
                            clean_c = content[:120].replace('\n', ' ').encode('ascii', 'replace').decode('ascii')
                            print(f"    CONTENT: \"{clean_c}\"")
else:
    print("Fastlane metadata directory not found!")
