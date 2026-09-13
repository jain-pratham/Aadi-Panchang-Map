import os
import re
from xml.etree import ElementTree as ET

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"

print("=== 1. LAUNCHER ICON SEARCH ===")
for root, dirs, files in os.walk(os.path.join(repo_root, "app", "src")):
    for f in files:
        if "ic_launcher" in f or "icon" in f:
            path = os.path.relpath(os.path.join(root, f), repo_root)
            print(f"ICON FILE: {path}")

print("\n=== 2. SPLASH SCREEN RESOURCE SEARCH ===")
for root, dirs, files in os.walk(os.path.join(repo_root, "app", "src")):
    for f in files:
        if "splash" in f.lower() or "logo" in f.lower():
            path = os.path.relpath(os.path.join(root, f), repo_root)
            print(f"SPLASH/LOGO FILE: {path}")

print("\n=== 3. APP RES DRAWABLE AUDIT ===")
res_dir = os.path.join(repo_root, "app", "src", "main", "res")
for root, dirs, files in os.walk(res_dir):
    for f in files:
        if f.endswith(('.png', '.jpg', '.jpeg', '.webp', '.xml', '.svg')):
            rel_dir = os.path.basename(root)
            path = os.path.relpath(os.path.join(root, f), repo_root)
            print(f"RES [{rel_dir}]: {f} -> {path}")

print("\n=== 4. ASSETS AUDIT (app/src/main/assets) ===")
assets_dir = os.path.join(repo_root, "app", "src", "main", "assets")
if os.path.exists(assets_dir):
    for root, dirs, files in os.walk(assets_dir):
        for f in files:
            path = os.path.relpath(os.path.join(root, f), repo_root)
            print(f"ASSET FILE: {path}")

print("\n=== 5. FASTLANE AUDIT ===")
fastlane_dir = os.path.join(repo_root, "fastlane")
if os.path.exists(fastlane_dir):
    for root, dirs, files in os.walk(fastlane_dir):
        for f in files:
            path = os.path.relpath(os.path.join(root, f), repo_root)
            print(f"FASTLANE FILE: {path}")

print("\n=== 6. OTHER ROOT ASSETS (assets/) ===")
root_assets_dir = os.path.join(repo_root, "assets")
if os.path.exists(root_assets_dir):
    for root, dirs, files in os.walk(root_assets_dir):
        for f in files:
            path = os.path.relpath(os.path.join(root, f), repo_root)
            print(f"ROOT ASSET FILE: {path}")
