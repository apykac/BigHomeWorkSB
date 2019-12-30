package ru.ccc;

/**
 * Класс контейнер, содержит константы для работы программы, чтобы не создовать копии одних и техже данных
 */
class ConstantContainer {
    /**
     * @param punctuation массив символов пунктуации и пробел
     * @param s_X нужны для записи результата в файл
     */
    public static final String punctuation = " !\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~";
    public static final byte[] s_1 = "[".getBytes();
    public static final byte[] s_2 = "] -> \"".getBytes();
    public static final byte[] s_3 = "\" @".getBytes();
    public static final byte[] s_4 = System.lineSeparator().getBytes();

    private ConstantContainer() {
    }
}
