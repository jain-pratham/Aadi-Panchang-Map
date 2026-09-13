import os
import re

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"

url_pattern = re.compile(r'https?://[^\s"<>\'\(\)]+')

found_urls = []

for root, dirs, files in os.walk(repo_root):
    if ".git" in root or "build" in root:
        continue
    for f in files:
        if f.endswith(('.java', '.kt', '.xml', '.txt', '.md', '.html', '.json', '.properties', '.gradle', '.sh')):
            fp = os.path.join(root, f)
            rel_p = os.path.relpath(fp, repo_root)
            with open(fp, "r", encoding="utf-8", errors="ignore") as file:
                for line_num, line in enumerate(file, 1):
                    for match in url_pattern.findall(line):
                        found_urls.append((rel_p, line_num, match))

print(f"Total URL references found: {len(found_urls)}\n")

url_counts = {}
for rel_p, line_num, url in found_urls:
    url_counts[url] = url_counts.get(url, 0) + 1

for url, count in sorted(url_counts.items(), key=lambda x: x[1], reverse=True):
    clean_u = url[:100].encode('ascii', 'replace').decode('ascii')
    print(f"{count:4d}x | {clean_u}")
