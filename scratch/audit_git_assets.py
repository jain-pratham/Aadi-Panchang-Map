import subprocess
import os

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"

asset_files = [
    "app/src/main/res/drawable/logo.png",
    "app/src/main/res/drawable/loading_logo.png",
    "app/src/main/res/drawable/stardroid_big_image.webp",
    "app/src/main/res/drawable-large/stardroid_big_image.jpg",
    "app/src/main/res/mipmap-hdpi/ic_launcher.webp",
    "app/src/main/res/mipmap-hdpi/ic_launcher_foreground.webp",
    "app/src/main/res/mipmap-hdpi/skymap_logo_new.webp",
    "app/src/main/res/raw/timetravel.mp3",
    "app/src/main/res/raw/timetravelback.mp3",
    "assets/drawable/skymap_logo.png",
    "assets/drawable/stardroid_logo.png",
    "assets/drawable/stardroid_big_image.png",
    "assets/raw/materialize.wav",
    "fastlane/metadata/android/en-US/images/icon.png",
    "fastlane/metadata/android/en-US/images/featureGraphic.png"
]

print("=== GIT HISTORY FOR KEY ASSETS ===")
for asset in asset_files:
    full_path = os.path.join(repo_root, asset)
    if os.path.exists(full_path):
        try:
            cmd = f'git log -n 1 --format="%h | %an | %ad | %s" -- "{asset}"'
            res = subprocess.check_output(cmd, cwd=repo_root, shell=True).decode('utf-8').strip()
            print(f"{asset:60s} -> {res}")
        except Exception as e:
            print(f"{asset:60s} -> Error: {e}")
    else:
        print(f"{asset:60s} -> FILE DOES NOT EXIST")
