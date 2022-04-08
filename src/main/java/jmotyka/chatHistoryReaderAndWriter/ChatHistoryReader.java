package jmotyka.chatHistoryReaderAndWriter;

import java.io.File;
import java.util.Map;

public interface ChatHistoryReader {

    <K, V> Map<K, V> read(File file);

}