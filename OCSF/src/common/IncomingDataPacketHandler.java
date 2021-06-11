package common;

import java.util.ArrayList;

public interface IncomingDataPacketHandler {

    public ArrayList<Object> CheckRequestExecuteCreateResponce(Object msg);

    public ArrayList<Object> ParsingDataPacket(DataPacket dataPacket);
}
