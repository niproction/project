package server;

public interface GroupBehavior {
	public void add(GroupMember gm);
	public void detach(GroupMember gm);
	public void notifyMembers(Object msg); // notify group member
}
