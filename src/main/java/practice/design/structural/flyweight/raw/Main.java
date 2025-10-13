package practice.design.structural.flyweight.raw;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Типи токенів
enum TokenType {
    KEYWORD, IDENTIFIER, STRING, NUMBER, COMMENT
}

// Стиль (зараз зберігається у кожному токені)
class Style {
    final String colorHex;
    final String bgHex;
    final boolean bold;
    final boolean italic;

    Style(String colorHex, String bgHex, boolean bold, boolean italic) {
        this.colorHex = colorHex;
        this.bgHex = bgHex;
        this.bold = bold;
        this.italic = italic;
    }
}

// Токен з дубльованим стилем
class Token {
    final int start, end;
    final TokenType type;
    final String lexeme;
    final Style style; // ❌ дублюємо однакові об’єкти тисячі разів

    Token(int start, int end, TokenType type, String lexeme, Style style) {
        this.start = start;
        this.end = end;
        this.type = type;
        this.lexeme = lexeme;
        this.style = style;
    }
}

class Highlighter {
    // Для простоти: "підсвічуємо" примітивно
    public List<Token> highlight(String source) {
        List<Token> out = new ArrayList<>();
        // Стилі щоразу створюються заново (ще й різними компонентами у реальному житті)
        Style kw = new Style("#3366FF", null, true, false);
        Style id = new Style("#222222", null, false, false);
        Style str = new Style("#CC5500", null, false, false);
        Style num = new Style("#AA00AA", null, false, false);
        Style com = new Style("#888888", null, false, true);

        // Умовний розбір (демо)
        String[] parts = source.split("\\s+");
        int pos = 0;
        for (String p : parts) {
            int start = pos;
            int end = pos + p.length();
            Token t;
            if (Objects.equals(p, "if") || Objects.equals(p, "else") || Objects.equals(p, "return")) {
                t = new Token(start, end, TokenType.KEYWORD, p, kw);
            } else if (p.matches("\".*\"")) {
                t = new Token(start, end, TokenType.STRING, p, str);
            } else if (p.matches("\\d+")) {
                t = new Token(start, end, TokenType.NUMBER, p, num);
            } else if (p.startsWith("//")) {
                t = new Token(start, end, TokenType.COMMENT, p, com);
            } else {
                t = new Token(start, end, TokenType.IDENTIFIER, p, id);
            }
            out.add(t);
            pos = end + 1; // +пробіл
        }
        return out;
    }
}

public class Main {
    public static void main(String[] args) {
        String src = "if x return 42 // note";
        Highlighter h = new Highlighter();
        var tokens = h.highlight(src);
        System.out.println("Tokens: " + tokens.size());
        // Уяви, що тут 500К токенів — пам’ять росте через дублювання Style
    }
}
