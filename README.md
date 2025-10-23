# 🧠 PK Helper

A comprehensive **RuneLite plugin** designed to enhance your PvP combat experience with real-time opponent tracking, freeze timers, prayer recommendations, and intelligent gear highlighting.

---

## ⚙️ Features

### 🧊 Freeze Tracking
- **Personal Freeze Timer:** Displays a countdown timer with an ice barrage icon above your character when frozen  
- **Opponent Freeze Timer:** Track when your opponent is frozen with a visual countdown  
- **Screen Flash:** Optional flash effect when you get frozen (customizable color and duration)  
- **Adjustable Positioning:** Configure X/Y offsets for freeze icons to suit your screen layout  

---

### 🙏 Prayer Assistance
- **Auto-Highlight:** Automatically highlights the correct protection prayer based on your opponent’s equipped weapon  
- **Smart Detection:** Recognizes melee (🔴 red), ranged (🟢 green), and magic (🔵 blue) weapons  
- **Real-Time Updates:** Instantly updates when opponents switch weapons  

---

### ⚔️ Opponent Tracking
- **Weapon Detection:** Displays your opponent’s current weapon in real time  
- **Combat Style Indicator:** Shows their attack style with color-coded borders:  
  - 🔴 **Melee**  
  - 🟢 **Ranged**  
  - 🔵 **Magic**  
- **Persistent Tracking:** Maintains lock on your current opponent, won’t switch if another player attacks you  

---

### 🎒 Inventory Highlights
- **Gear Recognition:** Automatically highlights weapons and armor in your inventory by combat style  
- **Custom Item IDs:** Add custom item IDs for gear not automatically detected  
- **Visual Clarity:** Clear, color-coded borders for fast gear recognition  

---

### 📍 Farcast Tiles
- **Optimal Positioning:** Highlights tiles where you can cast magic but opponent can’t attack back with rapid ranged  
- **Distance-Based Colors:**
  - 8 tiles = **Optimal** (just outside rapid range)  
  - 9 tiles = **Safe**  
  - 10 tiles = **Max magic range**
- **Display Modes:** Always, Magic Weapon Only, or Never  
- **Adjustable Opacity:** Customizable tile border opacity (default 30%)  

---

## 🛠️ Configuration

### 🧊 Freeze Settings
- Toggle personal and opponent freeze timers  
- Enable/disable screen flash  
- Customize flash color, opacity, and duration  
- Adjust freeze icon positioning with X/Y offset sliders  

---

### 🙏 Prayer & Enemy Highlights
- Toggle prayer highlights  
- Toggle opponent border highlighting  
- Colors automatically match combat styles  

---

### 📍 Farcast Tiles
- Choose display mode (Never / Always / Magic Weapon Only)  
- Customize colors for each distance tier (8, 9, 10 tiles)  
- Adjust tile opacity and border visibility  

---

### 🎒 Inventory Highlights
- Toggle inventory gear highlighting  
- Add **custom item IDs** for each combat style:
  - **Custom Magic IDs:** `comma,separated,ids`
  - **Custom Ranged IDs:** `comma,separated,ids`
  - **Custom Melee IDs:** `comma,separated,ids`

