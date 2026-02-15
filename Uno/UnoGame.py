#Imports
import random
from enum import Enum, auto

class Color(Enum):
    RED = auto()
    YELLOW = auto()
    GREEN = auto()
    BLUE = auto()
    WILD = auto()

class Value(Enum):
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
    SKIP = auto()
    REVERSE = auto()
    DRAW_TWO = auto()
    WILD = auto()
    WILD_DRAW_FOUR = auto()

class Card:
    def __init__(self, color: Color, value: Value):
        self.color = color
        self.value = value

    def is_wild(self) -> bool:
        return self.value in (Value.WILD, Value.WILD_DRAW_FOUR)

    def is_playable_on(self, top_card: "Card | None", current_color: Color) -> bool:
        if self.is_wild():
            return True
        if self.color == current_color:
            return True
        if top_card is None:
            return True
        return self.value == top_card.value

    @staticmethod
    def get_points(card: "Card") -> int:
        # Official UNO scoring. [web:23][web:55]
        if card.value in (Value.WILD, Value.WILD_DRAW_FOUR):
            return 50
        if card.value in (Value.DRAW_TWO, Value.SKIP, Value.REVERSE):
            return 20
        mapping = {
            Value.ZERO: 0,
            Value.ONE: 1,
            Value.TWO: 2,
            Value.THREE: 3,
            Value.FOUR: 4,
            Value.FIVE: 5,
            Value.SIX: 6,
            Value.SEVEN: 7,
            Value.EIGHT: 8,
            Value.NINE: 9,
        }
        return mapping.get(card.value, 0)

    @staticmethod
    def get_color_key(card: "Card") -> str:
        if card.color == Color.WILD:
            return "WILD"
        return card.color.name

    def __str__(self) -> str:
        if self.color == Color.WILD:
            return self.value.name
        return f"{self.color.name} {self.value.name}"
      
class Deck:
    def __init__(self):
        self.cards: list[Card] = []
        self.reset()

    def reset(self):
        self.cards.clear()
        for color in (Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE):
            # One ZERO per color
            self.cards.append(Card(color, Value.ZERO))
            # Two of 1–9 + actions. [web:23]
            for _ in range(2):
                for val in (
                    Value.ONE, Value.TWO, Value.THREE, Value.FOUR,
                    Value.FIVE, Value.SIX, Value.SEVEN, Value.EIGHT, Value.NINE,
                    Value.SKIP, Value.REVERSE, Value.DRAW_TWO
                ):
                    self.cards.append(Card(color, val))
        # Wilds
        for _ in range(4):
            self.cards.append(Card(Color.WILD, Value.WILD))
            self.cards.append(Card(Color.WILD, Value.WILD_DRAW_FOUR))
        self.shuffle()

    def shuffle(self):
        random.shuffle(self.cards)

    def is_empty(self) -> bool:
        return not self.cards

    def draw(self) -> "Card | None":
        if not self.cards:
            return None
        return self.cards.pop()

    def size(self) -> int:
        return len(self.cards)

class Player:
    def __init__(self, name: str):
        self.name = name
        self.hand: list[Card] = []

    def draw_card(self, deck: Deck):
        c = deck.draw()
        if c is not None:
            self.hand.append(c)

    def play_card(self, index: int) -> Card:
        return self.hand.pop(index)

    def has_won_round(self) -> bool:
        return len(self.hand) == 0

    def hand_size(self) -> int:
        return len(self.hand)

    def total_hand_points(self) -> int:
        return sum(Card.get_points(c) for c in self.hand)

class AIPlayer(Player):
    def choose_card_index(self, top_card: Card | None, current_color: Color) -> int:
        playable_indexes = [
            i for i, c in enumerate(self.hand)
            if c.is_playable_on(top_card, current_color)
        ]
        if not playable_indexes:
            return -1

        # Prefer non-wild cards. [web:59]
        for idx in playable_indexes:
            if not self.hand[idx].is_wild():
                return idx
        return playable_indexes[0]

    def choose_wild_color(self) -> Color:
        counts = {Color.RED: 0, Color.YELLOW: 0, Color.GREEN: 0, Color.BLUE: 0}
        for c in self.hand:
            if c.color in counts:
                counts[c.color] += 1
        best_color = Color.RED
        best_count = -1
        for color, count in counts.items():
            if count > best_count:
                best_count = count
                best_color = color
        return best_color

def sort_hand(hand: list[Card]) -> None:
    """
    Sorts a hand in-place:
    - group by color total points (ascending),
    - then by individual card points. [web:68]
    """
    def color_total(h: list[Card], key: str) -> int:
        return sum(
            Card.get_points(c) for c in h
            if Card.get_color_key(c) == key
        )

    def key_func(card: Card):
        ck = Card.get_color_key(card)
        return (
            color_total(hand, ck),
            Card.get_points(card),
            card.value.value   # stable-ish within same points
        )

    hand.sort(key=key_func)

TARGET_SCORE = 500  

class UnoGame:
    def __init__(self):
        self.players: list[Player] = []
        self.deck: Deck | None = None
        self.discard_pile: list[Card] = []
        self.current_player_index: int = 0
        self.direction: int = 1
        self.current_color: Color | None = None
        self.top_card: Card | None = None
        self.round_over: bool = False
        self.points_mode: bool = False
        self.scores = [0, 0]  # human, AI

    def start(self):
        print("=== UNO with Simple AI (Python) ===")
        print("Choose mode:")
        print("1) Classic multi-round (first to 500 points)")
        print("2) Single-round (first to go out wins)")
        choice = self._read_mode_choice()
        self.points_mode = (choice == 1)

        human = Player("You")
        ai = AIPlayer("AI")
        self.players = [human, ai]

        if self.points_mode:
            self._play_points_match()
        else:
            self._play_single_round()

        print("Thanks for playing!")

    def _read_mode_choice(self) -> int:
        while True:
            try:
                val = int(input("> ").strip())
                if val in (1, 2):
                    return val
            except ValueError:
                pass
            print("Enter 1 or 2:")

    def _setup_new_round(self):
        self.deck = Deck()
        self.discard_pile.clear()
        for p in self.players:
            p.hand.clear()

        for _ in range(7):
            for p in self.players:
                p.draw_card(self.deck)

        # flip first non-wild
        self.top_card = self.deck.draw()
        while self.top_card is not None and self.top_card.is_wild():
            self.discard_pile.append(self.top_card)
            self.top_card = self.deck.draw()
        if self.top_card is None:
            raise RuntimeError("Deck exhausted while picking starting card")

        self.discard_pile.append(self.top_card)
        self.current_color = self.top_card.color

        self.current_player_index = 0
        self.direction = 1
        self.round_over = False

        print("\nNew round starting...")
        print(f"Starting card: {self.top_card} | Current color: {self.current_color.name}")

    def _play_single_round(self):
        self._setup_new_round()
        while not self.round_over:
            self._take_turn()

    def _play_points_match(self):
        match_over = False
        while not match_over:
            self._setup_new_round()
            while not self.round_over:
                self._take_turn()

            winner_index = self._find_round_winner_index()
            if winner_index == -1:
                print("Round ended with no winner? (should not happen)")
                return

            round_points = self._calculate_round_points_for_winner(winner_index)
            self.scores[winner_index] += round_points

            print(f"{self.players[winner_index].name} wins the round and earns {round_points} points.")
            print(f"Scores: {self.players[0].name}={self.scores[0]} | "
                  f"{self.players[1].name}={self.scores[1]}")

            if self.scores[winner_index] >= TARGET_SCORE:
                print(f"{self.players[winner_index].name} reaches {TARGET_SCORE} points and wins the match!")
                match_over = True
            else:
                input("Press Enter to start the next round...")

    def _find_round_winner_index(self) -> int:
        for i, p in enumerate(self.players):
            if not p.hand:
                return i
        return -1

    def _calculate_round_points_for_winner(self, winner_index: int) -> int:
        total = 0
        for i, p in enumerate(self.players):
            if i == winner_index:
                continue
            for c in p.hand:
                total += Card.get_points(c)
        return total

    def _take_turn(self):
        deck = self.deck
        assert deck is not None

        current = self.players[self.current_player_index]
        print(f"\n--- {current.name}'s turn ---")
        print(f"Top card: {self.top_card} | Current color: {self.current_color.name}")
        print(f"Deck size: {deck.size()}")

        if isinstance(current, AIPlayer):
            self._handle_ai_turn(current)
        else:
            self._handle_human_turn(current)

        if current.has_won_round():
            print(f"{current.name} goes out!")
            self.round_over = True

    def _handle_human_turn(self, human: Player):
        deck = self.deck
        assert deck is not None

        while True:
            sort_hand(human.hand)
            print("Your hand:")
            for i, c in enumerate(human.hand):
                print(f"[{i}] {c}")
            print("Enter card index to play, or -1 to draw:")

            idx = self._read_int()
            if idx == -1:
                human.draw_card(deck)
                print("You drew a card.")
                self._advance_to_next_player()
                return
            if idx < 0 or idx >= len(human.hand):
                print("Invalid index.")
                continue

            chosen = human.hand[idx]
            if not chosen.is_playable_on(self.top_card, self.current_color):
                print("You cannot play that card.")
                continue

            played = human.play_card(idx)
            print(f"You played: {played}")
            self._apply_played_card(played, human, is_human=True)
            return

    def _handle_ai_turn(self, ai: AIPlayer):
        deck = self.deck
        assert deck is not None

        sort_hand(ai.hand)
        idx = ai.choose_card_index(self.top_card, self.current_color)
        if idx == -1:
            ai.draw_card(deck)
            print("AI draws a card.")
            self._advance_to_next_player()
            return

        chosen = ai.play_card(idx)
        print(f"AI plays: {chosen}")
        self._apply_played_card(chosen, ai, is_human=False)

    def _apply_played_card(self, card: Card, player: Player, is_human: bool):
        self.discard_pile.append(card)
        self.top_card = card

        if not card.is_wild():
            self.current_color = card.color

        if card.value in (Value.WILD, Value.WILD_DRAW_FOUR):
            if isinstance(player, AIPlayer):
                chosen = player.choose_wild_color()
                print(f"AI chooses color: {chosen.name}")
            else:
                chosen = self._prompt_for_color()
            self.current_color = chosen

        player_count = len(self.players)

        if card.value == Value.SKIP:
            if player_count == 2:
                skipped = self._get_next_player_index()
                print(f"{self.players[skipped].name} is skipped! You play again.")
                # same player index
            else:
                print("Next player is skipped!")
                self._advance_to_next_player()

        elif card.value == Value.REVERSE:
            if player_count == 2:
                skipped = self._get_next_player_index()
                print(f"Reverse acts as Skip in 2-player. "
                      f"{self.players[skipped].name} is skipped! You play again.")
                # same player index
            else:
                print("Direction reversed!")
                self.direction *= -1
                self._advance_to_next_player()

        elif card.value == Value.DRAW_TWO:
            victim_idx = self._get_next_player_index()
            victim = self.players[victim_idx]
            print(f"{victim.name} draws 2 cards and loses a turn!")
            victim.draw_card(self.deck)
            victim.draw_card(self.deck)
            if player_count == 2:
                # same player goes again
                pass
            else:
                self.current_player_index = victim_idx
                self._advance_to_next_player()

        elif card.value == Value.WILD_DRAW_FOUR:
            victim_idx = self._get_next_player_index()
            victim = self.players[victim_idx]
            print(f"{victim.name} draws 4 cards and loses a turn!")
            for _ in range(4):
                victim.draw_card(self.deck)
            if player_count == 2:
                # same player goes again
                pass
            else:
                self.current_player_index = victim_idx
                self._advance_to_next_player()

        else:
            self._advance_to_next_player()

        if player.hand_size() == 1:
            if is_human:
                print("UNO!")
            else:
                print("AI says UNO!")


    def _advance_to_next_player(self):
        self.current_player_index = self._get_next_player_index()

    def _get_next_player_index(self) -> int:
        size = len(self.players)
        nxt = (self.current_player_index + self.direction) % size
        return nxt

    def _prompt_for_color(self) -> Color:
        print("Choose color: 0=RED, 1=YELLOW, 2=GREEN, 3=BLUE")
        while True:
            val = self._read_int()
            mapping = {
                0: Color.RED,
                1: Color.YELLOW,
                2: Color.GREEN,
                3: Color.BLUE,
            }
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
    game = UnoGame()
    game.start()
