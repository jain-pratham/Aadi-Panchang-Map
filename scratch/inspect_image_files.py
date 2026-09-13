from PIL import Image
import os

images = [
    r"C:\Users\jainp\.gemini\antigravity-ide\brain\6f06c298-a4bf-41c5-95a6-a43d4538da2b\.user_uploaded\media_1789228503219.jpg",
    r"C:\Users\jainp\.gemini\antigravity-ide\brain\6f06c298-a4bf-41c5-95a6-a43d4538da2b\.user_uploaded\media_1789228538979.jpg",
    r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1\app\src\main\res\drawable\loading_logo.png",
    r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1\app\src\main\res\drawable\logo.png"
]

for img_path in images:
    if os.path.exists(img_path):
        try:
            with Image.open(img_path) as im:
                size = os.path.getsize(img_path)
                print(f"PATH: {img_path}")
                print(f"  Format: {im.format}, Mode: {im.mode}, Size: {im.size} (W={im.width}, H={im.height}), FileSize: {size} bytes\n")
        except Exception as e:
            print(f"Error reading {img_path}: {e}")
    else:
        print(f"NOT FOUND: {img_path}")
