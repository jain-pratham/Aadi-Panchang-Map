import os
from PIL import Image

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"

# Source attached files
icon_src_path = r"C:\Users\jainp\.gemini\antigravity-ide\brain\6f06c298-a4bf-41c5-95a6-a43d4538da2b\.user_uploaded\media_1789228538979.jpg"
splash_src_path = r"C:\Users\jainp\.gemini\antigravity-ide\brain\6f06c298-a4bf-41c5-95a6-a43d4538da2b\.user_uploaded\media_1789228503219.jpg"

print(f"Loading icon source from: {icon_src_path}")
icon_img = Image.open(icon_src_path).convert("RGBA")

print(f"Loading splash source from: {splash_src_path}")
splash_img = Image.open(splash_src_path).convert("RGB")

# 1. Main app icon PNG
main_logo_path = os.path.join(repo_root, "app", "src", "main", "res", "drawable", "logo.png")
icon_img.resize((1024, 1024), Image.LANCZOS).save(main_logo_path, "PNG")
print(f"Saved: {main_logo_path} (1024x1024 PNG)")

# 2. Fastlane Play Store Icon (512x512 PNG)
fastlane_icon_path = os.path.join(repo_root, "fastlane", "metadata", "android", "en-US", "images", "icon.png")
os.makedirs(os.path.dirname(fastlane_icon_path), exist_ok=True)
icon_img.resize((512, 512), Image.LANCZOS).save(fastlane_icon_path, "PNG")
print(f"Saved: {fastlane_icon_path} (512x512 PNG)")

# 3. Main Splash Poster PNG & WebP / JPG
splash_png_path = os.path.join(repo_root, "app", "src", "main", "res", "drawable", "loading_logo.png")
splash_img.resize((1080, 1920), Image.LANCZOS).save(splash_png_path, "PNG")
print(f"Saved: {splash_png_path} (1080x1920 PNG)")

stardroid_big_webp_path = os.path.join(repo_root, "app", "src", "main", "res", "drawable", "stardroid_big_image.webp")
splash_img.resize((1080, 1920), Image.LANCZOS).save(stardroid_big_webp_path, "WEBP", quality=90)
print(f"Saved: {stardroid_big_webp_path} (1080x1920 WEBP)")

stardroid_big_jpg_path = os.path.join(repo_root, "app", "src", "main", "res", "drawable-large", "stardroid_big_image.jpg")
os.makedirs(os.path.dirname(stardroid_big_jpg_path), exist_ok=True)
splash_img.resize((1080, 1920), Image.LANCZOS).save(stardroid_big_jpg_path, "JPEG", quality=90)
print(f"Saved: {stardroid_big_jpg_path} (1080x1920 JPG)")

# 4. Mipmap Densities
mipmap_densities = {
    "mipmap-mdpi": (48, 108),
    "mipmap-hdpi": (72, 162),
    "mipmap-xhdpi": (96, 216),
    "mipmap-xxhdpi": (144, 324),
    "mipmap-xxxhdpi": (192, 432)
}

app_res = os.path.join(repo_root, "app", "src", "main", "res")

for folder, (icon_sz, adaptive_sz) in mipmap_densities.items():
    folder_path = os.path.join(app_res, folder)
    os.makedirs(folder_path, exist_ok=True)

    # Legacy icon / skymap_logo_new
    ic_img = icon_img.resize((icon_sz, icon_sz), Image.LANCZOS)
    
    # Save ic_launcher.webp & skymap_logo_new.webp & ic_launcher_round.webp & skymap_logo_new_round.webp
    for name in ["ic_launcher.webp", "skymap_logo_new.webp", "ic_launcher_round.webp", "skymap_logo_new_round.webp"]:
        out_p = os.path.join(folder_path, name)
        ic_img.save(out_p, "WEBP", quality=90)
        print(f"Saved: {out_p} ({icon_sz}x{icon_sz} WEBP)")

    # Foreground adaptive icon (square centered)
    fg_img = icon_img.resize((adaptive_sz, adaptive_sz), Image.LANCZOS)
    for name in ["ic_launcher_foreground.webp", "skymap_logo_new_foreground.webp"]:
        out_p = os.path.join(folder_path, name)
        fg_img.save(out_p, "WEBP", quality=90)
        print(f"Saved: {out_p} ({adaptive_sz}x{adaptive_sz} WEBP)")

print("All branding assets successfully generated and replaced!")
