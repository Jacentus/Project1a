package jmotyka.chatHistoryReaderAndWriter;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ChatHistoryReader {

    <K, V extends List> Map<K, V> readFromFile(File file);

}
