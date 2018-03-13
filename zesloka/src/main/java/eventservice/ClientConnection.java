package eventservice;

import com.google.gson.JsonSyntaxException;
import executor.command.Command;
import executor.command.CommandProcessorManager;
import executor.command.GlobalCommand;
import executor.command.parameters.CommandParams;
import executor.command.utilcommands.ErrorCommand;
import executor.command.utilcommands.GetParamsCommand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ClientConnection implements IClientConnection {
    private static Integer id = 0;
    protected boolean isActive = true;
    protected BufferedReader input;
    protected BufferedWriter output;
    private Integer uniqueId;


    /**
     * Direct Client/Server Connection for bidirectional data transfer
     * @param in Input for reading data from client.
     * @param out Output stream wor writing to client.
     */
    public ClientConnection(BufferedReader in, BufferedWriter out) {
        this.input = in;
        this.output = out;
        this.uniqueId = id++;
    }

    @Override
    public void close() throws IOException {
        isActive = false;
        input.close();
        output.close();
    }

    @Override
    public void write(String msg) {
        try {
            output.write(msg + "\r\n");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer getId() {
        return this.uniqueId;
    }

    /**
     * Method is responsible for reading and figuring out what command has been called.
     * If it fails it rises an Error command with error message
     * or if it can match input with any registered method
     * it responds with GetParamsCommand what returns
     * required parameters of input key received.
     *
     * Failure command output is redirected to
     * provided outputStream for this connection.
     *
     * As this method could block while reading input
     * it is must to execute it in separate thread.
     */
    void waitForMsg() { //TODO: this is getting huge.. its time to refactor.
        try {
            while(true) {
                String cmd = input.readLine();
                if(isActive && cmd != null) {
                    CommandParams cp;
                    try {
                        cp = new CommandParams(cmd);
                        addGlobalParams(cp);
                        processCommand(cp.getRootCommand(), cp);
                    } catch (JsonSyntaxException e) {
                        System.out.println("error occurred");
                        Command c = Command.getCommand(cmd); // try to find command without parsing
                        if(c.getKey() == Command.CMD_NONE) { // command does not exists, initialize to error
                            ErrorCommand.riseError(e.getMessage(), String.valueOf(getId()));
                        } else { // command found so we can create params command for this command
                            c = Command.getCommand(GetParamsCommand.CMD_GET_PARAMS);
                            cp = new CommandParams();
                            cp.addValue(c.getKey(), Command.PARAM_CMD_KEY , cmd);
                            addGlobalParams(cp);
                            processCommand(c, cp);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addGlobalParams(CommandParams cp) {
        cp.addValue(
                Command.CMD_GLOBAL, GlobalCommand.PARAM_CLIENT_ID,
                String.valueOf(getId()));
    }

    private void processCommand(Command c, CommandParams cp) {
        CommandProcessorManager.getInstance().processCommand(c.setParams(cp));
    }
}
