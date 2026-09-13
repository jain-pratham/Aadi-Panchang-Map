import os

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"
search_dirs = [
    os.path.join(repo_root, "app", "src"),
    os.path.join(repo_root, "datamodel", "src"),
    os.path.join(repo_root, "tools", "src")
]

keywords = ["sky map", "skymap", "stardroid", "skymap_logo", "stardroid_logo"]

results = []

for s_dir in search_dirs:
    if os.path.exists(s_dir):
        for root, dirs, files in os.walk(s_dir):
            for f in files:
                if f.endswith(('.java', '.kt', '.xml', '.json', '.properties', '.gradle', '.html')):
                    full_p = os.path.join(root, f)
                    rel_p = os.path.relpath(full_p, repo_root)
                    with open(full_p, "r", encoding="utf-8", errors="ignore") as fp:
                        for line_num, line in enumerate(fp, 1):
                            line_lower = line.lower()
                            if any(kw in line_lower for kw in keywords):
                                results.append((rel_p, line_num, line.strip()))

print(f"Total occurrences found in source/res files: {len(results)}\n")
for rel_p, line_num, line in results:
    clean_line = line[:120].encode('ascii', 'replace').decode('ascii')
    print(f"{rel_p}:{line_num} | {clean_line}")
