package server;

import java.util.ArrayList;

import common.examInitiated;



// TODO: Auto-generated Javadoc
/**
 * The Class Group - to gather a group with the same purpose to notify them with some specific message for them, for example: students that attending the same exam
 * so notify them about extra time if approved, etc...
 */
public class Group implements GroupBehavior {
	/** The members. */
	private ArrayList<GroupMember> members = new ArrayList<>();
	
	/**
	 * Adds the ob(member) to the group.
	 *
	 * @param ob - Observer(Student,Teacher,Principal)
	 */
	@Override
	public void add(GroupMember gm) {
		members.add(gm);
		System.out.println("added member"+gm.toString());
	}

	/**
	 * Detach/remove the ob(member) from the group.
	 *
	 * @param index - index of the member group to be removed
	 */
	@Override
	public void detach(GroupMember gm) {
		int index=-1;
		for (int i = 0; i < members.size(); i++) {
			if(members.get(i).equals(gm))
			{
				index=i;
				System.out.println(members.remove(i));
				System.out.println("removed member at index "+i);
			}
		}
		if(index>0)
			System.out.println("removed member");
		
		System.out.println("Group size: "+members.size());
	}

	/**
	 * Notify members of the group with some message(DataPacket).
	 *
	 * @param msg - type of Object that will be passed later DataPacket
	 */
	@Override
	public void notifyMembers(Object msg) {
		int count=0;
		for (GroupMember observer : members) {
			observer.notify(msg);
			System.out.println("member notified "+count);
			count++;
		}
	}
	
	public static void addMemberToGroup(GroupMember gm, int exam)
	{
		System.out.println("exam id "+exam);
		if(CEMSServer.OnGoingExams!=null && !CEMSServer.OnGoingExams.contains(exam))
		{
			// there is no such on going exam so create a new group for this exam and add it to studentsOfOnGoingExam_Groups
			// also add it to the
			CEMSServer.OnGoingExams.add(exam); // added the new exam
			
			//create new group of students for this exam
			Group new_group = new Group();
			new_group.add(gm);
			
			CEMSServer.studentsOfOnGoingExam_Groups.add(new_group); // add this new group
			// to be able to update him about the extra time and other informations
		}
		else
		{
			// there is already a group of student live doing this exam so search for this for this idex of exam..
			// to know to which group to add the student
			int i = 0,index=0;
			for (; i < CEMSServer.OnGoingExams.size(); i++) {
				if(CEMSServer.OnGoingExams.equals(exam))
					index=i;
			}
			
			//add the the client and the user to this group
			CEMSServer.studentsOfOnGoingExam_Groups.get(index).add(gm);
		}
	}
	
	public static void deleteMemberFromGroup(GroupMember gm, int exam_initiated_ID)
	{
		System.out.println("exam id "+exam_initiated_ID);
		if(CEMSServer.OnGoingExams!=null && !CEMSServer.OnGoingExams.contains(exam_initiated_ID))
		{
			// there is no such on going exam 
			System.out.println("there is no such on going exam,so the student is not connected to nothing");
		}
		else
		{
			// there is already a group of student live doing
			// to know from which group to remove the student
			int i = 0,index=-1;
			for (; i < CEMSServer.OnGoingExams.size(); i++) {
				if(CEMSServer.OnGoingExams.get(i).equals(exam_initiated_ID))
				{
					index=i;
					System.out.println("found at index :"+index);
				}
			}
			if(index>-1)
				//remove the student from the ongoing exams group
				CEMSServer.studentsOfOnGoingExam_Groups.get(index).detach(gm);
			else
				System.out.println("Error not found this exam..");
		}
	}
}
