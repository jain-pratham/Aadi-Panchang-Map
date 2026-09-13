import os

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"

search_dirs = [
    os.path.join(repo_root, "fastlane"),
    os.path.join(repo_root, "app", "src", "main", "res"),
    os.path.join(repo_root, "assets")
]

targets = [
    "buymeacoffee",
    "skymapdevs@gmail.com",
    "stardroid.app",
    "@skymapdevs",
    "jainpratham4050@gmail.com"
]

print("=== REPOSITORY POST-CLEANUP VERIFICATION ===")
for t in targets:
    matches = []
    for s_dir in search_dirs:
        if os.path.exists(s_dir):
            for root, dirs, files in os.walk(s_dir):
                for f in files:
                    fp = os.path.join(root, f)
                    rel_p = os.path.relpath(fp, repo_root)
                    with open(fp, "r", encoding="utf-8", errors="ignore") as file:
                        for line_num, line in enumerate(file, 1):
                            if t.lower() in line.lower():
                                matches.append((rel_p, line_num, line.strip()))
    print(f"\nKeyword '{t}': Found {len(matches)} occurrences")
    for rel_p, line_num, line in matches[:10]:
        clean_l = line[:100].encode('ascii', 'replace').decode('ascii')
        print(f"  {rel_p}:{line_num} | {clean_l}")
