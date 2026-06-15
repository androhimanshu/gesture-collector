package com.touchtrace.app.storage;

import com.touchtrace.app.model.GestureEntry;
import com.touchtrace.app.model.SessionMeta;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Thread-safe, async, append-only JSON Lines writer. */
public final class JsonlWriter {
    private final File file;
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private BufferedWriter writer;

    public JsonlWriter(File file) { this.file = file; }

    public void open(SessionMeta meta) {
        io.execute(() -> {
            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true), StandardCharsets.UTF_8));
                writeLine(meta.toJson().toString());
            } catch (Exception ignored) { }
        });
    }

    public void enqueue(GestureEntry g) {
        io.execute(() -> {
            try { writeLine(g.toJson().toString()); }
            catch (Exception ignored) { }
        });
    }

    private void writeLine(String line) throws Exception {
        if (writer == null) return;
        writer.write(line);
        writer.newLine();
        writer.flush();
    }

    public void close() {
        io.execute(() -> {
            try { if (writer != null) writer.close(); } catch (Exception ignored) { }
        });
        io.shutdown();
    }

    public File getFile() { return file; }
}
