package practice.design.structural.flyweight.solution;

import java.util.*;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {
        String src = "if x return 42 // note";
        Highlighter h = new Highlighter();
        var tokens = h.highlight(src);
        System.out.println("Tokens: " + tokens.size());
    }
}

record Style(String colorHex, String bgHex, boolean bold, boolean italic) {}

enum TokenType {
    KEYWORD(p -> Objects.equals(p, "if") || Objects.equals(p, "else") || Objects.equals(p, "return")),
    STRING(p -> p.matches("\".*\"")),
    NUMBER(p -> p.matches("\\d+")),
    COMMENT(p -> p.startsWith("//")),
    IDENTIFIER;

    private final Predicate<String> rgxMatcher;

    TokenType(Predicate<String> rgxMatcher) {
        this.rgxMatcher = rgxMatcher;
    }

    TokenType() {
        this.rgxMatcher = p -> false;
    }

    public static TokenType getByString(String text) {
        for (TokenType t : TokenType.values()) {
            if(t.rgxMatcher.test(text)) return t;
        }
        return IDENTIFIER;
    }
}

record Token(int start, int end, TokenType type, String lexeme, Style style) { }

class StyleFactory {
    private static final Map<TokenType, Style> styles = new EnumMap<>(TokenType.class);

    private StyleFactory() {}

    public static Style getStyle(TokenType type) {
        Style style = styles.get(type);
        if (style == null) {
            style = initStyle(type);
            styles.put(type, style);
        }

        return style;
    }

    private static Style initStyle(TokenType type) {
        return switch (type) {
            case KEYWORD -> new Style("#3366FF", null, true, false);
            case STRING -> new Style("#222222", null, false, false);
            case NUMBER -> new Style("#CC5500", null, false, false);
            case COMMENT -> new Style("#AA00AA", null, false, false);
            default -> new Style("#888888", null, false, true);
        };
    }
}

class Highlighter {
    public List<Token> highlight(String source) {
        List<Token> out = new ArrayList<>();

        String[] parts = source.split("\\s+");
        int position = 0;
        for (String part : parts) {
            int start = position;
            int end = position + part.length();
            TokenType tokenType = TokenType.getByString(part);
            out.add(new Token(start, end, tokenType, part, StyleFactory.getStyle(tokenType)));
            position = end + 1;
        }
        return out;
    }
}
