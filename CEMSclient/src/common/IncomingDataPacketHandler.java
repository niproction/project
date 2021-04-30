package common;


public interface IncomingDataPacketHandler {

    public DataPacket CheckRequestExecuteCreateResponce(Object msg);

    public DataPacket ParsingDataPacket(DataPacket dataPacket);
}
