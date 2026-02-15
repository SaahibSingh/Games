def get_value(card):
    card = card.lower().strip()
    if 'draw 4' in card or 'wild draw 4' in card:
        return 50
    if any(x in card for x in ['+2', 'draw 2', 'skip', 'reverse', 'wild']):
        return 20
    parts = card.split()
    if len(parts) >= 2 and parts[1].isdigit():
        return int(parts[1])
    return 0

def get_color(card):
    card = card.lower().strip()
    if 'wild' in card or 'draw 4' in card:
        return 'Wild'
    parts = card.split()
    if parts:
        first = parts[0][0].upper()
        if first in 'RGBY':
            return first
    return 'Wild'

cards = []
n = int(input("Enter number of cards: "))
for i in range(n):
    card = input(f"Enter card {i+1} (e.g., 'red 3', 'wild draw 4'): ")
    cards.append(card)

def color_total(color):
    return sum(get_value(c) for c in cards if get_color(c) == color)

# Sort by color total asc, then value asc (stable)
sorted_cards = sorted(cards, key=lambda c: (color_total(get_color(c)), get_value(c)))

print("\nSorted hand (lowest color total points first, then by value):")
for card in sorted_cards:
    print(card)
