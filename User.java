package facebook;

import java.util.ArrayList;
import java.util.*;
import java.io.*;
import java.io.File;
/**
A class representing an account and all that can be done with it while on the facebook webpage. Is created in the {@link UserRepository}.
@author Dominic
*/
public class User {

	private String username;
	private String email;
	private String bday;
	private String password;
	private ArrayList<User> friends;
	private PhotoAlbum photos;
	private ArrayList<Group> usersGroups;
	private Inbox usersInbox;
	private String profilePic;
	//private ArrayList<Notification> notifications;
	private ArrayList<String> hobbies;
	private ArrayList<User> fRequests;
	private Wall usersWall;
	private ArrayList<Settings> settings;
	private ArrayList<Post> newsFeed;

	public User(String u, String e, String pwd)
	{
		username = u;
		email = e;
		password = pwd;
		this.bday = "N/A";
		profilePic = "profile.png";
		friends = new ArrayList<User>();
		photos = new PhotoAlbum(this);
		usersGroups = new ArrayList<Group>();
		usersInbox = new Inbox(this);
	//	notifications = new ArrayList<Notification>();
		hobbies = new ArrayList<String>();
		fRequests = new ArrayList<User>();
		usersWall = new Wall();
		settings = new ArrayList<Settings>();
		newsFeed = new ArrayList<Post>();
	}	

	public String getUsername()
	{
		return username; 
	}	

	public String getProfilePic(){
		return profilePic;
	}
	public String getPassword()
        {
		return password;
        }
	public String getBirthday(){
		return bday;
	}
	public String getEmail()
        {
		return email;
        }

	public ArrayList<Group> getGroups()
        {
		return usersGroups;
        }
	
	public ArrayList<User> getFriends()
        {
		return friends;
        }

	public Inbox getInbox()
	{
		return usersInbox;
	}
	public ArrayList<Post> getNewsFeed(){
		for(int x = 0; x < usersWall.getPosts().size(); x++){
			newsFeed.add(usersWall.getPosts().get(x));
			for(int y = 0; x < friends.size(); y++){
				for(int z = 0; z < friends.get(y).getWall().getPosts().size(); z++){
					newsFeed.add(friends.get(y).getWall().getPosts().get(z));
				}
			}
		}
		return newsFeed;
	}
	/* No longer needed
	public ArrayList<Notification> getNotifications()
	{
		return notifications;
	}
	*/

	public ArrayList<String> getHobbies()
	{
		return hobbies;
	}

	public PhotoAlbum getPhotoAlbum()
	{
		return photos;
		
	}

	public Wall getWall()
	{
		return usersWall;
	}

	public ArrayList<User> getFriendRequests()
	{
		return fRequests;
	}

	public void addHobby(String h)
	{
		hobbies.add(h);
		save();	
	}

	public void addGroup(Group g)
	{
		usersGroups.add(g);
	}

	public void addPhoto(String p)
	{
		photos.addPhoto(p);
		save();
	}
	
	public void sendMessage(int numberConv, InboxMessage message){
		Conversation addToConvo = usersInbox.getConversations().get(numberConv);
		addToConvo.getMessages().add(message);
		save();
	}
	public void startNewConvo(User user, User userTwo, String text, String subject){
		Conversation convo = new Conversation(user, userTwo, subject);
		InboxMessage mess = new InboxMessage(text, user, userTwo);
		convo.getMessages().add(mess);
		userTwo.getInbox().getConversations().add(convo);
		usersInbox.getConversations().add(convo);
		save();
	}	
/* No longer needed
	public void addNotification(Notification n)
	{
		notifications.add(n);
	}
*/

	public void deleteHobby(String h)
	{
		hobbies.remove(h);
		//save();
	}

        public void deletePhoto(String p)
        {
                photos.removePhoto(p);
		//save();
        }

	public void deleteGroup(Group g)
	{
		usersGroups.remove(g);
	}

	/**
	Sends a friend request to another user. Requests is added to the receiver's list of request and now has the option to either {@link User #acceptFriendRequest} or {@link User #rejectFriendRequest}
	@param u User who will be the receiver of the friend request.
	*/
	public void sendFriendRequest(User u)
	{
		u.fRequests.add(this);
		u.save();
	}

/**
A receiver of a friend request agrees to be 'friends' with the {@link FriendRequest} sender. The receiver is now added to the sender's list of friends and vice versa. All privileges available to friends are now enabled. The initial friend request will be removed from the list of pending friend requests
@param fr FriendRequest object sent from another {@link User}
*/
	public void acceptFriendRequest(User fr)
	{
		friends.add(fr);
		fr.getFriends().add(this);
		//fr.accept(); does nothing
		//u.notify()
		fRequests.remove(fr);
		save();
	}

/**
A receiver of a friend request agrees to be 'friends' with the {@link FriendRequest} sender. The receiver is now added to the sender's list of friends and vice versa. All privileges available to friends are now enabled. The initial 
friend request will be removed from the list of pending friend requests
@param fr FriendRequest object sent from another {@link User}
*/
	public void rejectFriendRequest(User fr)
	{
		//fr.reject(); 
		fRequests.remove(fr);
		save();
	}

/**
Creates a new {@link Group} with this {@link User} being listed as the creator. The creator is given special privileges that ordinary group members do not have.
@param groupName a {@link String} that will be used to designate the group from other groups
*/
	public void createGroup(String groupName)
	{
		//Group g = new Group(groupName, username, settings); 
		//usersGroups.add(g);
	}
	public void setProfilePic(String profilePic){
		this.profilePic = profilePic;
		save();
	}
	public void setBday(String month, String day, String year){
		bday = (month + '/' + day + '/' + year);
		save();
	}

	public void addPost(Post p)
	{
		usersWall.getPosts().add(p);
		save();
	}	

	public void save()
        {
	String extension = "/home/tbaird/public_html/FB_Stuff/";
                try{
                        File file = new File(extension + email + ".user");
                        FileWriter fw = new FileWriter(file);
                        PrintWriter pw = new PrintWriter(fw);

                        pw.println(username);
                        pw.println("~~");

                        pw.println(email);
                        pw.println("~~");

                        pw.println(password);
                        pw.println("~~");

                        pw.println(bday);
                        pw.println("~~");

			 //Save friends in friends list
                        for(int i = 0; i < friends.size(); i++)
                        {
                                pw.println(friends.get(i).getEmail());
                        }
                        pw.println("~~");

                        //Save hobbies in hobby list
                        for (int i = 0; i < hobbies.size(); i++)
                        {
                                pw.println(hobbies.get(i));
                        }
                        pw.println("~~");

                        //Save profilePicture
                        pw.println(profilePic);
                        pw.println("~~");

                        //Save friend Requests in fRequests
                        for(int i = 0; i < fRequests.size(); i++)
                        {
                                pw.println(fRequests.get(i).getEmail());
			         //Output date as string don't need
                            //    pw.println(fRequests.get(i).getDate());
                        }
                        pw.println("~~");

                        //Save posts in wall
                        for(int i = 0; i < usersWall.getPosts().size(); i++)
                        {
                                pw.println(usersWall.getPosts().get(i).getAuthor().getEmail());
                                pw.println(usersWall.getPosts().get(i).getRecipient().getEmail());
                                //Output date as string
                                pw.println(usersWall.getPosts().get(i).getDate());
                                pw.println(usersWall.getPosts().get(i).getText());
                        }
                        pw.println("~~");


                        pw.close();
                        }catch (Exception e)
                                                   {
                                System.out.println("HEY!!! Couldn't save " + username+ "!");
                                e.printStackTrace();

                        }
        }

        public void load(String filename) throws Exception
        {
		if(filename != null)
		{
			File userFile = new File("/home/tbaird/public_html/FB_Stuff/" + filename);
			FileReader fr = new FileReader(userFile);
			BufferedReader br = new BufferedReader(fr);
			UserRepository sameInstance = UserRepository.instance();

			//Get Username
			username = br.readLine();
			br.readLine();
			
			//Get Email
			// String cutFileName = filename.substring(0, filename.length()-5);
			email = br.readLine();
			br.readLine();
			
			//Get Password
			password = br.readLine();
			br.readLine();
	
			//Get Bday
			bday = br.readLine();
			br.readLine();

			//Loop to get friends
			String friendLine = br.readLine();
			while(!friendLine.equals("~~"))
			{
			String fEmail = friendLine;
			User friendToAdd = sameInstance.getUser(fEmail);
			friends.add(friendToAdd);
			friendLine = br.readLine();
			}

			//Loop for Hobbies
			String hobbyLine = br.readLine();
			while(!hobbyLine.equals("~~"))
			{
			String hobbyToAdd = hobbyLine;
			hobbies.add(hobbyToAdd);
			hobbyLine = br.readLine();
			}
	
			//Get Profile Pic
			profilePic = br.readLine();
			br.readLine();

			//Loop for Friend Requests
			String frLine = br.readLine();
			while(!frLine.equals("~~"))
			{
			String fEmail = frLine;
			//UserRepository sameInstance = UserRepository.instance();
                        User frToAdd = sameInstance.getUser(fEmail);
			fRequests.add(frToAdd);
			frLine = br.readLine();	
			}

			//Loop For Wall Posts
			String wallLine = br.readLine();
			while(!wallLine.equals("~~"))
			{
				String postAuthorEmail = wallLine;
				//UserRepository sameInstance = UserRepository.instance();
				User pAuthor = sameInstance.getUser(postAuthorEmail);
				String postRecipientEmail = br.readLine();
				User pRecipient = sameInstance.getUser(postRecipientEmail);
				String pDate = br.readLine();
				String pText = br.readLine();
				Post wPost = new Post(pAuthor,pRecipient,pText);
				wPost.setDate(pDate);
				//this.addPost(wPost);
				usersWall.getPosts().add(wPost);
				wallLine = br.readLine();
			}
		}
        }
}


