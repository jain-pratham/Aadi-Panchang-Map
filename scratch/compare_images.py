from PIL import Image
import os

img1_path = r"C:\Users\jainp\.gemini\antigravity-ide\brain\6f06c298-a4bf-41c5-95a6-a43d4538da2b\.user_uploaded\media_1789228503219.jpg"
img2_path = r"C:\Users\jainp\.gemini\antigravity-ide\brain\6f06c298-a4bf-41c5-95a6-a43d4538da2b\.user_uploaded\media_1789228538979.jpg"
loading_logo_path = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1\app\src\main\res\drawable\loading_logo.png"
logo_path = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1\app\src\main\res\drawable\logo.png"

print("--- User Uploaded Media ---")
print("Media 1 (Splash):", Image.open(img1_path).size, Image.open(img1_path).format)
print("Media 2 (Icon):  ", Image.open(img2_path).size, Image.open(img2_path).format)

print("\n--- Project Drawables ---")
print("loading_logo.png:", Image.open(loading_logo_path).size, Image.open(loading_logo_path).format)
print("logo.png:        ", Image.open(logo_path).size, Image.open(logo_path).format)
