package de.curse.allround.core.cloud.cli;

import de.curse.allround.core.cloud.cli.commands.HelpCommand;
import de.curse.allround.core.cloud.cli.commands.ListeGroupsCommand;
import de.curse.allround.core.cloud.cli.commands.ListNodesCommand;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter(AccessLevel.PRIVATE)
public class CommandManager {
    private final List<Command> commands;
    private final ExecutorService executorService;
    private final Terminal terminal;
    private final LineReader lineReader;
    private boolean running;

    public CommandManager() {
        this.commands = new ArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
        try {
            this.terminal = TerminalBuilder.builder().name("CloudNode").system(true).build();
            this.lineReader = LineReaderBuilder.builder().terminal(terminal).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){
        startInputScanner();

        //register commands
        registerCommand(new ListNodesCommand());
        registerCommand(new ListeGroupsCommand());
        registerCommand(new HelpCommand());
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void stop(){
        stopInputScanner();
    }

    public void startInputScanner() {
        running = false;
        running = true;
        executorService.submit(() -> {
            while (running) {
                onCommandLine(lineReader.readLine("[Input] » "));
            }
        });
    }

    public void stopInputScanner() {
        running = false;
        executorService.shutdownNow();
    }


    public void registerCommand(Command command) {
        AtomicBoolean alreadyPresent = new AtomicBoolean(false);
        commands.forEach(cmd -> {
            if (cmd.getName().equalsIgnoreCase(command.getName())) {
                alreadyPresent.set(true);
                return;
            }
            for (String alias : command.getAlias()) {
                if (Arrays.stream(cmd.getAlias()).anyMatch(s -> s.equalsIgnoreCase(alias))) {
                    alreadyPresent.set(true);
                    return;
                }
            }
        });
        if (alreadyPresent.get()) return;
        this.commands.add(command);
    }

    public Optional<Command> getCommand(String cmd) {
        return commands.stream().filter(command -> command.getName().equalsIgnoreCase(cmd) || Arrays.stream(command.getAlias()).anyMatch(s -> s.equalsIgnoreCase(cmd))).findFirst();
    }

    public void onCommandLine(@NotNull String line) {
        String[] lineParts = line.split(" ");
        if (lineParts.length == 0) return;
        String cmd = lineParts[0];
        Optional<Command> optionalCommand = getCommand(cmd);
        if (optionalCommand.isEmpty()) {
            System.out.println("No command found with this name. Try \"help\" for more information.");
            return;
        }
        if (lineParts.length <= 1) {
            optionalCommand.get().execute(new String[0]);
        } else optionalCommand.get().execute(Arrays.copyOfRange(lineParts, 1, lineParts.length - 1));
    }


}
