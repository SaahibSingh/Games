#Imports
import random
from enum import Enum, auto

class Side(Enum):
    LIGHT = auto()
    DARK = auto()

class FlipColor(Enum):
    # Light side colors
    LIGHT_PINK = auto()
    LIGHT_TEAL = auto()
    LIGHT_PURPLE = auto()
    LIGHT_ORANGE = auto()
    LIGHT_WILD = auto()

    # Dark side colors
    DARK_PINK = auto()
    DARK_TEAL = auto()
    DARK_PURPLE = auto()
    DARK_ORANGE = auto()
    DARK_WILD = auto()

class FlipValue(Enum):
    # Number values (shared concept)
    ZERO = auto()
    ONE = auto()
    TWO = auto()
    THREE = auto()
    FOUR = auto()
    FIVE = auto()
    SIX = auto()
    SEVEN = auto()
    EIGHT = auto()
    NINE = auto()

    # Light side specials
    LIGHT_SKIP = auto()
    LIGHT_REVERSE = auto()
    LIGHT_DRAW_ONE = auto()
    LIGHT_FLIP = auto()
    LIGHT_WILD = auto()
    LIGHT_WILD_DRAW_TWO = auto()

    # Dark side specials
    DARK_SKIP = auto()
    DARK_REVERSE = auto()
    DARK_DRAW_FIVE = auto()
    DARK_SKIP_EVERYONE = auto()
    DARK_FLIP = auto()
    DARK_WILD = auto()
    DARK_WILD_DRAW_COLOR = auto()

class FlipCard:
    #UNO Flip card with a light and dark face. 

    def __init__(self,
                 light_color: FlipColor, light_value: FlipValue,
                 dark_color: FlipColor, dark_value: FlipValue):
        self.light_color = light_color
        self.light_value = light_value
        self.dark_color = dark_color
        self.dark_value = dark_value

    def get_color(self, side: Side) -> FlipColor:
        return self.light_color if side == Side.LIGHT else self.dark_color

    def get_value(self, side: Side) -> FlipValue:
        return self.light_value if side == Side.LIGHT else self.dark_value

    def is_wild(self, side: Side) -> bool:
        v = self.get_value(side)
        return v in {
            FlipValue.LIGHT_WILD,
            FlipValue.LIGHT_WILD_DRAW_TWO,
            FlipValue.DARK_WILD,
            FlipValue.DARK_WILD_DRAW_COLOR
        }

    def is_flip_card(self, side: Side) -> bool:
        v = self.get_value(side)
        return v in {FlipValue.LIGHT_FLIP, FlipValue.DARK_FLIP}

    def is_playable_on(self,
                       top: "FlipCard | None",
                       side: Side,
                       current_color: FlipColor) -> bool:
        """
        UNO Flip play legality:
        - Wilds always playable.
        - Otherwise match current color, top color, or top value on the active side. 
        """
        if top is None:
            return True
        if self.is_wild(side):
            return True

        my_color = self.get_color(side)
        my_value = self.get_value(side)
        top_color = top.get_color(side)
        top_value = top.get_value(side)

        return (
            my_color == current_color or
            my_color == top_color or
            my_value == top_value
        )

    def __str__(self) -> str:
        return f"[L:{self.light_color.name} {self.light_value.name} | " \
               f"D:{self.dark_color.name} {self.dark_value.name}]"


# =====================
# Deck
# =====================

class FlipDeck:
    def __init__(self):
        self.cards: list[FlipCard] = []
        self.reset()

    def reset(self):
        self.cards.clear()
        self._add_number_cards()
        self._add_light_specials()
        self._add_dark_specials()
        self.shuffle()

    def _add_number_cards(self):
        light_colors = [
            FlipColor.LIGHT_PINK,
            FlipColor.LIGHT_TEAL,
            FlipColor.LIGHT_PURPLE,
            FlipColor.LIGHT_ORANGE
        ]
      
        dark_colors = [
            FlipColor.DARK_PINK,
            FlipColor.DARK_TEAL,
            FlipColor.DARK_PURPLE,
            FlipColor.DARK_ORANGE
        ]
      
        vals = [
            FlipValue.ONE, FlipValue.TWO, FlipValue.THREE, FlipValue.FOUR,
            FlipValue.FIVE, FlipValue.SIX, FlipValue.SEVEN, FlipValue.EIGHT,
            FlipValue.NINE
        ]
      
        for lc, dc in zip(light_colors, dark_colors):
            self.cards.append(FlipCard(lc, FlipValue.ZERO, dc, FlipValue.ZERO))
            for _ in range(2):
                for v in vals:
                    self.cards.append(FlipCard(lc, v, dc, v))

    def _add_light_specials(self):
        light_colors = [
            FlipColor.LIGHT_PINK,
            FlipColor.LIGHT_TEAL,
            FlipColor.LIGHT_PURPLE,
            FlipColor.LIGHT_ORANGE
        ]
        dark_colors = [
            FlipColor.DARK_PINK,
            FlipColor.DARK_TEAL,
            FlipColor.DARK_PURPLE,
            FlipColor.DARK_ORANGE
        ]
        for lc, dc in zip(light_colors, dark_colors):
            # Skip
            self.cards.append(FlipCard(lc, FlipValue.LIGHT_SKIP, dc, FlipValue.DARK_SKIP))
            self.cards.append(FlipCard(lc, FlipValue.LIGHT_SKIP, dc, FlipValue.DARK_SKIP))
            # Reverse
            self.cards.append(FlipCard(lc, FlipValue.LIGHT_REVERSE, dc, FlipValue.DARK_REVERSE))
            self.cards.append(FlipCard(lc, FlipValue.LIGHT_REVERSE, dc, FlipValue.DARK_REVERSE))
            # Draw One / Draw Five
            self.cards.append(FlipCard(lc, FlipValue.LIGHT_DRAW_ONE, dc, FlipValue.DARK_DRAW_FIVE))
            self.cards.append(FlipCard(lc, FlipValue.LIGHT_DRAW_ONE, dc, FlipValue.DARK_DRAW_FIVE))
            # Flip
            self.cards.append(FlipCard(lc, FlipValue.LIGHT_FLIP, dc, FlipValue.DARK_FLIP))

        # Wilds: light wild + wild draw two vs dark wild + wild draw color. [web:69][web:81]
        for _ in range(4):
            self.cards.append(FlipCard(
                FlipColor.LIGHT_WILD, FlipValue.LIGHT_WILD,
                FlipColor.DARK_WILD, FlipValue.DARK_WILD
            ))
            self.cards.append(FlipCard(
                FlipColor.LIGHT_WILD, FlipValue.LIGHT_WILD_DRAW_TWO,
                FlipColor.DARK_WILD, FlipValue.DARK_WILD_DRAW_COLOR
            ))

    def shuffle(self):
        random.shuffle(self.cards)

    def is_empty(self) -> bool:
        return not self.cards

    def draw(self) -> FlipCard | None:
        if not self.cards:
            return None
        return self.cards.pop()

    def size(self) -> int:
        return len(self.cards)

class FlipPlayer:
    def __init__(self, name: str):
        self.name = name
        self.hand: list[FlipCard] = []

    def draw_card(self, deck: FlipDeck):
        c = deck.draw()
        if c is not None:
            self.hand.append(c)

    def play_card(self, index: int) -> FlipCard:
        return self.hand.pop(index)

    def has_won_round(self) -> bool:
        return len(self.hand) == 0

    def hand_size(self) -> int:
        return len(self.hand)


class FlipAIPlayer(FlipPlayer):
    def choose_card_index(self,
                          top: FlipCard | None,
                          side: Side,
                          current_color: FlipColor) -> int:
        best = -1
        for i, c in enumerate(self.hand):
            if c.is_playable_on(top, side, current_color):
                if not c.is_wild(side):
                    return i
                if best == -1:
                    best = i
        return best

    def choose_wild_color(self, side: Side) -> FlipColor:
        return FlipColor.LIGHT_PINK if side == Side.LIGHT else FlipColor.DARK_PINK

class UnoFlipGame:
    def __init__(self):
        self.players: list[FlipPlayer] = []
        self.deck: FlipDeck | None = None
        self.side: Side = Side.LIGHT
        self.top_card: FlipCard | None = None
        self.current_color: FlipColor | None = None
        self.current_player_index: int = 0
        self.round_over: bool = False

    def start(self):
        print("=== UNO FLIP (Python, single round, 2 players) ===")
        self.players = [
            FlipPlayer(input("Enter your name: ")),
            FlipAIPlayer("AI")
        ]
        self._setup_new_round()
        while not self.round_over:
            self._take_turn()
        print("Game over!")

    def _setup_new_round(self):
        self.deck = FlipDeck()
        self.side = Side.LIGHT
        for p in self.players:
            p.hand.clear()

        for _ in range(7):
            for p in self.players:
                p.draw_card(self.deck)

        # flip first valid non-wild, non-flip as starting card for clarity
        self.top_card = self.deck.draw()
        while (self.top_card is None or
               self.top_card.is_wild(self.side) or
               self.top_card.is_flip_card(self.side)):
            self.top_card = self.deck.draw()
        self.current_color = self.top_card.get_color(self.side)

        self.current_player_index = 0
        self.round_over = False

        print("\nStarting round on LIGHT side.")
        print(f"Starting card (LIGHT): "
              f"{self.top_card.get_color(self.side).name} "
              f"{self.top_card.get_value(self.side).name}")

    # ---- main loop --- #
    def _take_turn(self):
        """
        Executes a single turn for the current player.
        """
        deck = self.deck
        assert deck is not None

        player = self.players[self.current_player_index]
        print(f"\n--- {player.name}'s turn ---")
        print(f"Side: {self.side.name}")
        print(f"Top card: {self.top_card.get_color(self.side).name} "
              f"{self.top_card.get_value(self.side).name}")
        print(f"Current color: {self.current_color.name}")
        print(f"Deck size: {deck.size()}")

        if isinstance(player, FlipAIPlayer):
            self._handle_ai_turn(player)
        else:
            self._handle_human_turn(player)

        if player.has_won_round():
            print(f"{player.name} goes out and wins the round!")
            self.round_over = True

    def _handle_human_turn(self, player: FlipPlayer):
        """
        Shows the human player's hand, lets them draw or play, and validates the move.
        """
        deck = self.deck
        assert deck is not None

        while True:
            print("Your hand:")
            for i, c in enumerate(player.hand):
                print(f"[{i}] {c.get_color(self.side).name} {c.get_value(self.side).name}")
            print("Enter card index to play, or -1 to draw:")

            idx = self._read_int()
            if idx == -1:
                player.draw_card(deck)
                print("You drew a card.")
                self._advance_to_next_player()
                return
            if idx < 0 or idx >= len(player.hand):
                print("Invalid index.")
                continue

            card = player.hand[idx]
            if not card.is_playable_on(self.top_card, self.side, self.current_color):
                print("You cannot play that card.")
                continue

            played = player.play_card(idx)
            print(f"You played: {played.get_color(self.side).name} "
                  f"{played.get_value(self.side).name}")
            self._apply_played_card(played, player, is_human=True)
            return

    def _handle_ai_turn(self, ai: FlipAIPlayer):
        """
        Lets the AI choose and play a card, or draw if none is playable.
        """
        deck = self.deck
        assert deck is not None

        idx = ai.choose_card_index(self.top_card, self.side, self.current_color)
        if idx == -1:
            ai.draw_card(deck)
            print("AI draws a card.")
            self._advance_to_next_player()
            return

        played = ai.play_card(idx)
        print(f"AI plays: {played.get_color(self.side).name} "
              f"{played.get_value(self.side).name}")
        self._apply_played_card(played, ai, is_human=False)

    # ---- card effects ----

    def _apply_played_card(self, card: FlipCard, player: FlipPlayer, is_human: bool):
        """
        Sets the new top card, updates color, handles FLIP and wild effects,
        and advances to the next player.
        """
        self.top_card = card
        self.current_color = card.get_color(self.side)

        # FLIP card: just toggle side (logical flip). [web:69][web:70]
        if card.is_flip_card(self.side):
            print("FLIP! Switching sides.")
            self.side = Side.DARK if self.side == Side.LIGHT else Side.LIGHT

        # Wild color choice
        if card.is_wild(self.side):
            if isinstance(player, FlipAIPlayer):
                chosen = player.choose_wild_color(self.side)
                print(f"AI chooses color: {chosen.name}")
                self.current_color = chosen
            else:
                self.current_color = self._prompt_for_color()

        # TODO: extend to handle Draw One, Draw Five, Skip, Skip Everyone, etc.

        self._advance_to_next_player()
        if player.hand_size() == 1:
            print("UNO!" if is_human else "AI says UNO!")

    # ---- turn helpers ----

    def _advance_to_next_player(self):
        """
        Moves to the next player (2-player only: just toggles).
        """
        self.current_player_index = (self.current_player_index + 1) % len(self.players)

    # ---- input helpers ----

    def _prompt_for_color(self) -> FlipColor:
        """
        Asks the human which color to choose for a wild, depending on the current side.
        """
        if self.side == Side.LIGHT:
            print("Choose color: 0=PINK, 1=TEAL, 2=PURPLE, 3=ORANGE")
            mapping = {
                0: FlipColor.LIGHT_PINK,
                1: FlipColor.LIGHT_TEAL,
                2: FlipColor.LIGHT_PURPLE,
                3: FlipColor.LIGHT_ORANGE,
            }
        else:
            print("Choose color: 0=PINK, 1=TEAL, 2=PURPLE, 3=ORANGE")
            mapping = {
                0: FlipColor.DARK_PINK,
                1: FlipColor.DARK_TEAL,
                2: FlipColor.DARK_PURPLE,
                3: FlipColor.DARK_ORANGE,
            }

        while True:
            val = self._read_int()
            if val in mapping:
                return mapping[val]
            print("Invalid color.")

    def _read_int(self) -> int:
        while True:
            try:
                line = input("> ").strip()
                return int(line)
            except ValueError:
                print("Enter a valid integer:")


if __name__ == "__main__":
    game = UnoFlipGame()
    game.start()
