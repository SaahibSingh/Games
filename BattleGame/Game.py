#This is the BattleGame, which I originally did in Python
#This is more accurate, as it has the health represented as bars and includes colors
# Importing necessary libraries
import random
import time

# ANSI Color Helper Functions
def colored(text, color_code): return f"\033[{color_code}m{text}\033[0m"
def green(t): return colored(t, '92')
def red(t): return colored(t, '91')
def yellow(t): return colored(t, '93')
def cyan(t): return colored(t, '96')
def magenta(t): return colored(t, '95')
def blue(t): return colored(t, '94')

# Constants and Global Data
DEFENDER = "🛡️"
ATTACKER = "⚔️"
FIREBALL = "🔥"
ICE_SPEAR = "❄️"
LIGHTNING_STRIKE = "⚡"
HEALING_WINDS = "🌿"
EARTHQUAKE = "🌍"
POISON_DART = "☠️"
ULTIMATE_BLAST = "💥 Ultimate Blast"

# Attack Library
ATTACKS = [
    f"{ATTACKER} Sword Slash",
    f"{FIREBALL} Fireball",
    f"{DEFENDER} Shield Block",
    f"{ICE_SPEAR} Ice Spear",
    f"{LIGHTNING_STRIKE} Lightning Strike",
    f"{HEALING_WINDS} Healing Winds",
    f"{EARTHQUAKE} Earthquake",
    f"{POISON_DART} Poison Dart",
    ULTIMATE_BLAST
]

# Attack Damage Dictionary
ATTACK_DAMAGE = {
    f"{ATTACKER} Sword Slash": 15,
    f"{FIREBALL} Fireball": 20,
    f"{DEFENDER} Shield Block": 0,
    f"{ICE_SPEAR} Ice Spear": 20,
    f"{LIGHTNING_STRIKE} Lightning Strike": 20,
    f"{HEALING_WINDS} Healing Winds": -15,
    f"{EARTHQUAKE} Earthquake": 15,
    f"{POISON_DART} Poison Dart": 15,
    ULTIMATE_BLAST: 40
}

MAX_ATTACK_USE = 3
MAX_ULTIMATE_USE = 1
MAX_HP = 100

# Dramatic Messages (5 per category)

# Victory Messages
VICTORY_MSGS = [
    green("You emerge victorious! What a comeback! 🎉"),
    green("Triumphant! You crushed your foe! 🏆"),
    green("Victory is yours! Time for a dance! 💃"),
    green("You win! Fortune favors the bold! 🥇"),
    green("Winner winner, battle dinner! 🍗"),
]

# Defeat Messages
DEFEAT_MSGS = [
    red("Defeat... but legends are born from loss. 💀"),
    red("Crushed! But every champion once lost! 🦾"),
    red("The AI wins this time. Stay strong! 🚩"),
    red("Game over! The road to glory is never easy! 🚧"),
    red("You lost! Even your pet rock is disappointed! 🪨😭"),  # Funniest loss message
]

# Draw Messages
DRAW_MSGS = [
    yellow("It's a draw! Rivals to the end! 🤝"),
    yellow("A dramatic tie! Rematch?"),
    yellow("Stalemate! No victor, no vanquished."),
    yellow("Evenly matched, neither yields!"),
    yellow("Draw! Even the gods look confused!"),
]

# Attack Descriptions
ATTACK_DESCS = [
    "with a blazing fury",
    "unleashing full power",
    "in a whirlwind of chaos",
    "with surprising finesse",
    "with unstoppable force",
]

# Animation Frames for All Attacks
ANIMATION_FRAMES = {
    f"{ATTACKER} Sword Slash":
        [" ", " =", " ==", " ===", " ====", " =====", "======="],
    f"{FIREBALL} Fireball":
        [" ( ) ", " (🔥) ", "(🔥🔥)", " (🔥) ", " ( ) "],
    f"{ICE_SPEAR} Ice Spear":
        ["  ❄  ", "  ❄❄ ", " ❄❄❄ ", "  ❄❄ ", "   ❄  "],
    f"{LIGHTNING_STRIKE} Lightning Strike":
        ["   ⚡   ", "  ⚡⚡  ", " ⚡⚡⚡ ", "  ⚡⚡  ", "   ⚡   "],
    f"{POISON_DART} Poison Dart":
        ["  ->   ", " -->   ", " --->  ", " --→   ", "  →    "],
    ULTIMATE_BLAST:
        ["     *      ", "    ***     ", "   *****    ", "  *******   ",
         "   *****    ", "    ***     ", "     *      "],
    f"{HEALING_WINDS} Healing Winds":
        ["~", "~ 🌿 ~", "~ 🌿🌿 ~", "~ 🌿 ~", "~"],
    f"{DEFENDER} Shield Block":
        ["[   ]", "[───]", "[███]", "[───]", "[   ]"],
    f"{EARTHQUAKE} Earthquake":
        ["  🌍  ", " 🌍🌍 ", "🌍🌍🌍", " 🌍🌍 ", "  🌍  "],
}

# Utility & Display Functions

# Show Stats Function
def show_stats(stats):
    print(blue("\n===== GAME STATS ====="))
    print(green(f"Player Wins: {stats['player_wins']}"))
    print(red(f"AI Wins:      {stats['ai_wins']}"))
    print(yellow(f"Draws:       {stats['draws']}"))
    print(blue(f"Total Battles: {stats['battles']}"))
    print(blue("======================"))

# Show Menu Function
def show_menu():
    print(f"\n{blue('Welcome to Emoji Battle! 🎮')}")
    print(yellow("1. Start Game"))
    print(yellow("2. How to Play"))
    print(yellow("3. View Game Stats"))
    print(yellow("4. Exit"))

# How to Play Function
def how_to_play():
    print(f"""
{cyan('How to Play:')}
- Choose attacks each turn with limited uses to prevent spamming
- Shield Block reduces next incoming damage
- Poison Dart causes damage over time
- Ultimate Blast can be used only once when your HP drops below 30
- Play either fixed turns or until someone's HP reaches 0
- Enjoy the battle and good luck!
""")

# Play Again Function
def play_again():
    while True:
        choice = input(cyan("Battle again? (yes/no): ")).lower()
        if choice in ("yes", "y"):
            return True  # triggers full game restart but stats persist
        elif choice in ("no", "n"):
            print(magenta("Thanks for playing! 👋"))
            return False
        else:
            print(red("Please enter 'yes' or 'no'."))

# Get Game Style Function
def get_game_style():
    while True:
        print(yellow("Choose game mode:\n1: Number of Turns (5 turns)\n2: By Health (Fight until HP zero)"))
        c = input(cyan("Enter 1 or 2: "))
        if c in ("1", "2"):
            return c
        print(red("Invalid input. Try again."))

# Print Welcome Function
def print_welcome():
    name = input(cyan("Enter your name: "))
    print(magenta(f"👋 Welcome to Kingdom Clash, {name}! 🏰 Get ready to battle!"))
    
# Play Animation Function
def play_animation(avatar, message, frames, delay=0.1):
    print(f"{avatar} {message}")
    for frame in frames:
        print(frame)
        time.sleep(delay)
    print()

# Dramatic Attack Description Function
def dramatic_attack_desc(attack):
    return random.choice(ATTACK_DESCS)

# Show Attack Animation Function
def show_attack_animation(actor_label, attack):
    frames = ANIMATION_FRAMES.get(attack)
    if frames:
        play_animation(actor_label, f"uses {attack} {dramatic_attack_desc(attack)}!", frames)

# Display Health Function
def display_health(player_hp, ai_hp):
    print(f"\n{green('PLAYER HP:')}   {'🟩' * (player_hp // 5)}")
    print(f"{red('COMPUTER HP:')} {'🟥' * (ai_hp // 5)}")

# Apply Poison Function
def apply_poison(name, hp, poisoned):
    if poisoned:
        print(magenta(f"{name} suffers 5 poison damage!"))
        hp -= 5
    return hp

# Attack Menu Function
def attack_menu(player_hp, usage_tracker):
    print(f"\n{yellow('Choose your attack:')}")
    display_idx = 1
    idx_map = {}
    for i, at in enumerate(ATTACKS):
        if at == ULTIMATE_BLAST and player_hp >= 30:
            print(cyan(f"{display_idx}. {at} (Locked: HP >= 30)"))
            display_idx += 1
            continue

        max_use = MAX_ULTIMATE_USE if at == ULTIMATE_BLAST else MAX_ATTACK_USE
        used = usage_tracker.get(at, 0)
        dmg = ATTACK_DAMAGE[at]
        desc = f"Heals {-dmg} HP" if dmg < 0 else "Blocks next attack" if dmg == 0 else f"Deals {dmg} damage"

        if used >= max_use:
            print(red(f"{display_idx}. {at} ({desc}) - MAXED OUT"))
            display_idx += 1
            continue

        print(cyan(f"{display_idx}. {at} ({desc}) - Used {used}/{max_use}"))
        idx_map[display_idx] = i
        display_idx += 1

    while True:
        choice = input(cyan(f"Enter a number 1-{display_idx-1}: "))
        if choice.isdigit():
            choice_num = int(choice)
            if choice_num in idx_map:
                return idx_map[choice_num]
            else:
                print(red("That option is not selectable (maxed out or locked). Choose a valid attack."))
        else:
            print(red("Invalid choice. Enter the number corresponding to your attack."))

# AI Decision Making Function
def smart_ai_choice(player_hp, ai_hp, player_last_attack, ai_usage):
    healing = f"{HEALING_WINDS} Healing Winds"
    powerful = [
        f"{LIGHTNING_STRIKE} Lightning Strike",
        f"{FIREBALL} Fireball",
        f"{ICE_SPEAR} Ice Spear",
        f"{EARTHQUAKE} Earthquake",
        f"{POISON_DART} Poison Dart"
    ]
    shield = f"{DEFENDER} Shield Block"
    sword = f"{ATTACKER} Sword Slash"

    def allowed(a):
        max_use = MAX_ULTIMATE_USE if a == ULTIMATE_BLAST else MAX_ATTACK_USE
        return ai_usage.get(a, 0) < max_use

    if ai_hp < 30 and allowed(healing) and random.random() < 0.6:
        return healing

    choices = [a for a in powerful + [sword, shield] if allowed(a)]
    if player_hp < 30 and choices:
        weights = [0.25,0.22,0.18,0.16,0.15,0.02,0.02]
        if len(choices) != len(weights):
            weights = [1 / len(choices)] * len(choices)
        return random.choices(choices, weights=weights, k=1)[0]

    strong_moves = set(powerful)
    if player_last_attack in strong_moves and allowed(shield):
        block_choices = [shield] * 3 + choices
        return random.choice(block_choices) if block_choices else shield

    allowed_attacks = [a for a in ATTACKS[:-1] if allowed(a)]
    return random.choice(allowed_attacks) if allowed_attacks else healing

# Turns Mode Game Play
def play_turns_mode(stats, player_hp, ai_hp):
    player_block = ai_block = False
    player_poisoned = ai_poisoned = False
    player_usage, ai_usage = {}, {}
    player_last_attack = None

    for turn in range(1, 6):
        print(blue(f"\nTurn {turn}:"))
        display_health(player_hp, ai_hp)

        player_idx = attack_menu(player_hp, player_usage)
        player_attack = ATTACKS[player_idx]
        ai_attack = smart_ai_choice(player_hp, ai_hp, player_last_attack, ai_usage)

        # Player animation & action
        show_attack_animation(green("You"), player_attack)
        next_player_block = False
        if player_attack == f"{DEFENDER} Shield Block":
            next_player_block = True
            print(green("You brace with your shield."))
        elif player_attack == f"{HEALING_WINDS} Healing Winds":
            player_hp = min(player_hp + 15, MAX_HP)
            print(green("You heal for 15 HP!"))
        else:
            damage = ATTACK_DAMAGE[player_attack]
            if ai_block:
                print(yellow("AI blocks your attack! (damage reduced)"))
                damage //= 2
            ai_hp -= damage
            print(red(f"You hit AI for {damage} damage!"))
            if player_attack == f"{POISON_DART} Poison Dart" and random.random() < 0.4:
                print(magenta("You poisoned the AI!"))
                ai_poisoned = True
        player_usage[player_attack] = player_usage.get(player_attack, 0) + 1

        # AI animation & action
        show_attack_animation(red("AI"), ai_attack)
        next_ai_block = False
        if ai_attack == f"{DEFENDER} Shield Block":
            next_ai_block = True
            print(red("AI braces with its shield."))
        elif ai_attack == f"{HEALING_WINDS} Healing Winds":
            ai_hp = min(ai_hp + 15, MAX_HP)
            print(red("AI heals for 15 HP!"))
        else:
            damage = ATTACK_DAMAGE[ai_attack]
            if player_block:
                print(yellow("You block AI's attack! (damage reduced)"))
                damage //= 2
            player_hp -= damage
            print(red(f"AI hits you for {damage} damage!"))
            if ai_attack == f"{POISON_DART} Poison Dart" and random.random() < 0.4:
                print(magenta("AI poisoned you!"))
                player_poisoned = True
        ai_usage[ai_attack] = ai_usage.get(ai_attack, 0) + 1

        # Apply poison damage at end turn
        player_hp = apply_poison(green("You"), player_hp, player_poisoned)
        ai_hp = apply_poison(red("AI"), ai_hp, ai_poisoned)

        player_block = next_player_block
        ai_block = next_ai_block
        player_last_attack = player_attack

        # Check end game states
        if player_hp <= 0 and ai_hp <= 0:
            print(f"\n{random.choice(DRAW_MSGS)}")
            stats['draws'] += 1
            stats['battles'] += 1
            return 'draw'
        elif player_hp <= 0:
            print(f"\n{random.choice(DEFEAT_MSGS)}")
            stats['ai_wins'] += 1
            stats['battles'] += 1
            return 'loss'
        elif ai_hp <= 0:
            print(f"\n{random.choice(VICTORY_MSGS)}")
            stats['player_wins'] += 1
            stats['battles'] += 1
            return 'win'

    # After turns end – decide by HP
    print(cyan(f"\nGame Over! Your HP: {player_hp} | AI HP: {ai_hp}"))
    if player_hp > ai_hp:
        print(random.choice(VICTORY_MSGS))
        stats['player_wins'] += 1
        stats['battles'] += 1
        return 'win'
    elif player_hp < ai_hp:
        print(random.choice(DEFEAT_MSGS))
        stats['ai_wins'] += 1
        stats['battles'] += 1
        return 'loss'
    else:
        print(random.choice(DRAW_MSGS))
        stats['draws'] += 1
        stats['battles'] += 1
        return 'draw'

# Health Mode Game Play
def play_health_mode(stats, player_hp, ai_hp):
    player_block = ai_block = False
    player_poisoned = ai_poisoned = False
    player_usage, ai_usage = {}, {}
    player_last_attack = None

    while player_hp > 0 and ai_hp > 0:
        next_player_block = False
        next_ai_block = False

        display_health(player_hp, ai_hp)

        player_idx = attack_menu(player_hp, player_usage)
        player_attack = ATTACKS[player_idx]
        ai_attack = smart_ai_choice(player_hp, ai_hp, player_last_attack, ai_usage)

        # Player animation & action
        show_attack_animation(green("You"), player_attack)
        if player_attack == f"{DEFENDER} Shield Block":
            next_player_block = True
            print(green("You brace with your shield."))
        elif player_attack == f"{HEALING_WINDS} Healing Winds":
            player_hp = min(player_hp + 15, MAX_HP)
            print(green("You heal for 15 HP!"))
        else:
            damage = ATTACK_DAMAGE[player_attack]
            if ai_block:
                print(yellow("AI blocks your attack! (damage reduced)"))
                damage //= 2
            ai_hp -= damage
            print(red(f"You hit AI for {damage} damage!"))
            if player_attack == f"{POISON_DART} Poison Dart" and random.random() < 0.4:
                print(magenta("You poisoned the AI!"))
                ai_poisoned = True
        player_usage[player_attack] = player_usage.get(player_attack, 0) + 1

        # AI animation & action
        show_attack_animation(red("AI"), ai_attack)
        if ai_attack == f"{DEFENDER} Shield Block":
            next_ai_block = True
            print(red("AI braces with its shield."))
        elif ai_attack == f"{HEALING_WINDS} Healing Winds":
            ai_hp = min(ai_hp + 15, MAX_HP)
            print(red("AI heals for 15 HP!"))
        else:
            damage = ATTACK_DAMAGE[ai_attack]
            if player_block:
                print(yellow("You block AI's attack! (damage reduced)"))
                damage //= 2
            player_hp -= damage
            print(red(f"AI hits you for {damage} damage!"))
            if ai_attack == f"{POISON_DART} Poison Dart" and random.random() < 0.4:
                print(magenta("AI poisoned you!"))
                player_poisoned = True
        ai_usage[ai_attack] = ai_usage.get(ai_attack, 0) + 1

        # Apply poison damage at end turn
        player_hp = apply_poison(green("You"), player_hp, player_poisoned)
        ai_hp = apply_poison(red("AI"), ai_hp, ai_poisoned)

        player_block, ai_block = next_player_block, next_ai_block
        player_last_attack = player_attack

    # End of battle output
    print(cyan(f"\nGame Over! Your HP: {max(player_hp, 0)} | AI HP: {max(ai_hp, 0)}"))
    if player_hp <= 0 and ai_hp <= 0:
        print(random.choice(DRAW_MSGS))
        stats['draws'] += 1
        stats['battles'] += 1
        return 'draw'
    elif player_hp <= 0:
        print(random.choice(DEFEAT_MSGS))
        stats['ai_wins'] += 1
        stats['battles'] += 1
        return 'loss'
    else:
        print(random.choice(VICTORY_MSGS))
        stats['player_wins'] += 1
        stats['battles'] += 1
        return 'win'

# Main Game Loop
def play_game():
    stats = {'player_wins': 0, 'ai_wins': 0, 'draws': 0, 'battles': 0}
    while True:
        print_welcome()
        while True:
            show_menu()
            choice = input(cyan("Enter your choice (1-4): "))
            if choice == "1":
                player_hp, ai_hp = MAX_HP, MAX_HP
                print(magenta("Starting the game..."))
                while True:
                    style = get_game_style()
                    if style == "1":
                        play_turns_mode(stats, player_hp, ai_hp)
                    else:
                        play_health_mode(stats, player_hp, ai_hp)
                    again = play_again()
                    if again:
                        # break to restart the welcome/menu/gameplay loop (full reset game state, not stats)
                        break
                    else:
                        print(magenta("Thanks for playing! 👋"))
                        exit()
                # break to restart from welcome
                break
            elif choice == "2":
                how_to_play()
            elif choice == "3":
                show_stats(stats)
            elif choice == "4":
                print(magenta("Goodbye!"))
                exit()
            else:
                print(red("Invalid choice. Please select again."))

play_game()
