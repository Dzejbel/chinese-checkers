package client.chinesecheckers;

/**
 * Klasa ze statyczną metodą do formatowania i wstępnego interpretowania inputu gracza z konsoli
 */
public class UserInputInterpreter {

    /**
     * Statyczna metoda do formatowania i wstępnego interpretowania inputu gracza z konsoli
     * Jest to pośrednik między graczem (osobą) a serwerem
     */
    public static String interpretUserInput(String message) {
        message = message.trim();
        String formattedMessage;

        String command;
        String argument;

        int spaceIndex = message.indexOf(' ');
        if (spaceIndex == -1) {
            command = message;
            argument = "";
        } else {
            command = message.substring(0, spaceIndex);
            argument = message.substring(spaceIndex + 1).replaceAll("\\s+", "");
        }

        switch (command) {
            case "bye":
            case "exit":
                if (argument.equals("server")) {
                    formattedMessage = "exit server";
                    break;
                }
            case "quit":
                formattedMessage = "exit";
                break;

            case "help": 
                formattedMessage = "HELP";
                formattedMessage += "\tbye/exit/quit - leave the app\n";
                formattedMessage += "\tdraw - shows current state of the board\n";
                formattedMessage += "\thelp - shows this instruction\n";
                formattedMessage += "\tmove [arg],[arg]-[arg],[arg] - moves piece between chosen points\n";
                formattedMessage += "\tskip = skips your move\n";
                formattedMessage += "\tstart [arg] - starts new game for chosen number of people";
                break;

            case "start":
                try {
                    Integer.parseInt(argument);
                    formattedMessage = command + " " + argument;
                } catch (NumberFormatException e) {
                    formattedMessage = "ERROR: invalid argument. Try start [arg]";
                }
                break;

            case "draw":
                formattedMessage = command;
                break;

            case "skip":
                formattedMessage = command;
                break;

            case "move":
                try {
                    int[] args = new int[4];
                    args[0] = Integer.parseInt(argument.substring(0, argument.indexOf(',')));
                    args[1] = Integer.parseInt(argument.substring(argument.indexOf(',') + 1, argument.indexOf('-')));
                    args[2] = Integer.parseInt(argument.substring(argument.indexOf('-') + 1, argument.lastIndexOf(',')));
                    args[3] = Integer.parseInt(argument.substring(argument.lastIndexOf(',') + 1));
                    formattedMessage = command + " " + args[0] + "," + args[1] + "-" + args[2] + "," + args[3];
                } catch (Exception e) {
                    formattedMessage = "ERROR: invalid argument. Try move [arg],[arg]-[arg],[arg]";
                }
                break;

            default:
                formattedMessage = "ERROR: Unknown command. Try help to show all available commands.";
                break;
        }

        return formattedMessage;
    }
}
