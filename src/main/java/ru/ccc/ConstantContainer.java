package ru.ccc;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс контейнер, содержит константы для работы программы, чтобы не создовать копии одних и техже данных
 */
class ConstantContainer {
    /** массив символов пунктуации и пробел */
    public static final String PUNCTUATION = " !\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~";
    /** s_X нужны для записи результата в файл */
    public static final byte[] S_1 = "[".getBytes(StandardCharsets.UTF_8);
    public static final byte[] S_2 = "] -> \"".getBytes(StandardCharsets.UTF_8);
    public static final byte[] S_3 = "\" @".getBytes(StandardCharsets.UTF_8);
    public static final byte[] S_4 = System.lineSeparator().getBytes(StandardCharsets.UTF_8);

    private static final Set<Character> PUNCTUATION_SET;

    static {
        Set<Character> set = new HashSet<>();
        ConstantContainer.PUNCTUATION.chars().boxed()
                .map(e -> (char) e.shortValue())
                .forEach(set::add);
        PUNCTUATION_SET = Collections.unmodifiableSet(set);
    }

    private ConstantContainer() {
    }

    /**
     * метод проверки символа на то что он являет символом пунктуации или пробелом
     *
     * @param c символ для сравнения
     * @return символ пунктуации или пробел на переданный на вход символ
     */
    public static boolean isCharPunc(char c) {
        return PUNCTUATION_SET.contains(c);
    }
}
