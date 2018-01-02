package eventservice;

import com.google.gson.JsonSyntaxException;
import executor.command.Command;
import executor.command.CommandProcessorManager;
import executor.command.GlobalCommand;
import executor.command.parameters.CommandParams;
import executor.command.utilcommands.ErrorCommand;

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

    void waitForMsg() {
        try {
            while(true) {
                String cmd = input.readLine();
                if(isActive) {
                    CommandParams cp;

                    try {
                        cp = new CommandParams(cmd);
                        cp.addValue(
                                Command.CMD_GLOBAL, GlobalCommand.PARAM_CLIENT_ID,
                                String.valueOf(getId()));
                        CommandProcessorManager.getInstance().
                                processCommand(cp.getRootCommand().setParams(cp));
                    } catch (JsonSyntaxException e) {
                        Command errcmd = Command.getCommand(ErrorCommand.CAD_ERROR);
                        cp = new CommandParams();
                        cp.addValue(
                                errcmd.getKey(),
                                ErrorCommand.PARAM_ERROR,
                                e.getMessage());
                        cp.addValue(
                                Command.CMD_GLOBAL,
                                GlobalCommand.PARAM_CLIENT_ID,
                                String.valueOf(getId()));
                        CommandProcessorManager.getInstance().processCommand(errcmd.setParams(cp));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
