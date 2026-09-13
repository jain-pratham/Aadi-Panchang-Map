import subprocess

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"

cmd = 'git log --oneline -n 20'
res = subprocess.check_output(cmd, cwd=repo_root, shell=True).decode('utf-8').strip()
print("=== REPO COMMITS ===")
print(res)
