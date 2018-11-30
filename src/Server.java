import java.awt.Image;

import java.io.BufferedOutputStream;

import java.io.BufferedReader;

import java.io.DataInputStream;

import java.io.DataOutputStream;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.ObjectInputStream;

import java.io.OutputStream;

import java.io.PrintWriter;



import java.net.InetAddress;



import java.net.ServerSocket;



import java.net.Socket;



import java.util.HashMap;

//import InetAddress;

import java.util.Random;



import javax.imageio.ImageIO;

import javax.swing.JLabel;



/**

 * server message format :<all upper case letter> 

 * <SUBMITNAME>: give to client-submit name (must unique) 

 * <SUBMITTEAM>: give to client, if name is accepted- choose team 

 * <NAMEACCEPTED>: give to client if name is unique - name is accepted if not enter again. 

 * <GAMEFRAME>: give to client if name, ip,team_name is all - show game panel. 

 * <ALLIN>: give to client if meet number of users - start game. 

 * <SEQUENCE> : give to client - show user's sequence in game panel. 

 * <GIVECATEGORY>: give to client when game is start - show category to every user. 

 * <GIVEWORD>: give to client but only to first user- show word to first user. 

 * <SEND>: give to client if the user in turn?? - show painting panel.

 * <START>: ??

 * <>: give to client if new user is enter - show user list.

 * <SCORE>: give to client if last client is answer- update score.

 * <Aanswer>/<Banswer>: give from client after last client answer - check if the answer is correct.

 * <ANSWERCORRECT> : give to client when Aanswer/Banswer message is give then the answer is correct - send information to last user.

 * <ANSWERWRONG>:give to client when Aanswer/Banswer message is give then the answer is wrong - send information to last user.

 * <CANVAS>: ??

 * <OUT>:give to client when file transfer is end- socket for file close

 * <RECEIVE>:??

 * <AANSWERSHEET>/BAANSWERSHEET> give to client when last client have to answer - answer.

 */

public class Server {



	private static int asequence = 0, bsequence = 0;// sequence for each team.

	private static int acount = 0, bcount = 0;// specify user(by sequence)

	private static String[] names1 = new String[6];// name for user - using name list

	private static int user_howmuch = 0;// number of user

	private static final int PORT = 5880;// The relay sketch server port number- connect client

	private static int chattingcheck = 0;// check chatting is available.

	private static int ateamout = 0, bteamout = 0;// number of exit user.

	private static int a = 0, b = 0;// ???



	/*

	 * The string is the key(users)of clients in the chat room so that we can check

	 * that new clients are not registering name already in use. Then mapping

	 * users(users+team) and printwriter. users format=<A>name / <B>name

	 *

	 */

	private static HashMap<String, PrintWriter> users = new HashMap<String, PrintWriter>();

	private static HashMap<String, PrintWriter> team_a = new HashMap<String, PrintWriter>();

	private static HashMap<String, PrintWriter> team_b = new HashMap<String, PrintWriter>();



	private static String word[][] = new String[6][30];// entire word that read in file

	private static String category[] = new String[6];// entire category that read in file

	private static int score_a = 0;// score a team

	private static int score_b = 0;// score b team

	private static int choice_category;// choice category index number in category array.

	private static String choice_word[] = new String[30];// arrange randomly sequence by choice category word.

	private static int size_word = 0;// word size

	private static int indexword_a = 0;// sequence of a word index

	private static int indexword_b = 0;// sequence of b word index



	/*

	 * main function make category, word make socket and listen it accept then run

	 * handler.

	 * 

	 */

	public static void main(String[] args) throws Exception {

		System.out.println("The relay sketch game server is running.");



		random_category();// make category index



		ServerSocket listener = new ServerSocket(PORT);

		// 해당된 포트로 들어올 수 있게 함



		try {

			while (true) {

				new Handler(listener.accept()).start();
				

			}

		} finally {

			listener.close();

		}

	}



	private static class Handler extends Thread {

		private String name;

		private Socket socket;

		private BufferedReader in;

		private PrintWriter out;

		private String team;



		public Handler(Socket socket) {



			this.socket = socket;



		}



		public String getName(String name)



		{



			return this.name;



		}



		public String getTeam(String team)



		{



			return this.team;



		}



		/**

		 * 

		 * Services this thread's client by repeatedly requesting a

		 * 

		 * screen name until a unique one has been submitted, then

		 * 

		 * acknowledges the name and registers the output stream for

		 * 

		 * the client in a global set, then repeatedly gets inputs and

		 * 

		 * broadcasts them.

		 * 

		 */

		@SuppressWarnings("resource")

		public void run() {

			try {

				// Create character streams for the socket.

				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				out = new PrintWriter(socket.getOutputStream(), true);



				// Request a name from this client. Keep requesting until

				// a name is submitted that is not already used. Note that

				// checking for the existence of a name and adding the name

				// must be done while locking the set of users.

				while (true) {

					out.println("<SUBMITNAME>");

					name = in.readLine();

					if (name == null) {

						return;

					} // if(name ==null) 문 종료



					/* check name is unique */

					synchronized (users) {



						/* if name is unique, then choose team.(A,B) */

						if (!team_a.containsKey(name) && !team_b.containsKey(name)) {

							String temp = null;



							out.println("<SUBMITTEAM>");// server message: team choice

							team = in.readLine();



							/**

							 * team assign if a team is over 3(limit) then assign team B if not, assign team

							 * A put in hash map by team A , B user name format: "<A/B>"+name

							 */



							if (team.equals("A")) {

								if (a >= 3) {

									team_b.put(name, out);



									name = "<B> " + name;

									b++;

								}



								else {

									team_a.put(name, out);

									name = "<A> " + name;

									a++;

								}

							} // if(team==A)종료



							/**

							 * team assign if B team is over 3(limit) then assign team A if not, assign team

							 * B put in hash map by team A , B user name format:"<A/B>"+name

							 */

							if (team.equals("B")) {



								if (b >= 3) {

									team_a.put(name, out);

									name = "<A> " + name;

									a++;

								} else {

									team_b.put(name, out);



									name = "<B> " + name;

									b++;

								}



							} // if(team==b )종료



							out.println("<GAMEFRAME>");// now check all things, then game is start (game panel show).



							/*

							 * array list 있으니까 뺼까???? // Print new user info in chat room except new user's

							 * chat room for (PrintWriter writer : users.values()) {

							 * writer.println("MESSAGE " + "***" + name + "님이 입장하셨습니다. ***"); } //

							 * for(printwriter)

							 */

							users.put(name, out);



							/**

							 * Game preparation is complete and the game begins Step 1: give category and

							 * order to all users. : give word to first user and provide painting panel. and

							 * until end of the game chat is prohibited to prevent the outflow of answers.

							 **/

							if (users.size() + ateamout

									+ bteamout == 3/* &&team_a.size()+ateamout==3&&team_b.size()+bteamout==3 */) {

								for (PrintWriter writer : users.values()) {

									writer.println("<ALLIN>");

								} // for

								give_category("A");// send category to users of a team

								give_category("B");// send category to users of b team

							} // if(all in one)

							break;

						} // if문 (이름 확인)



					} // 싱크로



				} // while문



				out.println("<NAMEACCEPTED>");// - name is accepted(unique)



				// To every user send the information of other users.(team+name)

				names1[user_howmuch] = name;

				user_howmuch++;



				users.put(name, out);



				for (PrintWriter writer : users.values()) {

					writer.println("<TEAMVIEW>");

					writer.println(users.size());

					for (int i = 0; i < user_howmuch; i++) {

						writer.println(names1[i]);

					}

				}



				/**

				 * Accept messages from this client and broadcast same team member But game is

				 * start then chat is forbidden.

				 */



				int gamea = 0, gameb = 0;

				while (true) {

					String input = "";

					int incount_a = 0, incount_b = 0;



					/*

					 * gamea is 1 when the last person answer and next word show to first user, then

					 * gamea is 0.

					 * 

					 */

					if (gamea == 1) {

						for (PrintWriter writer : team_a.values()) {

							if (gamea == 1) /* send next word to first user. */

							{



								writer.println("<GIVEWORD>");

								writer.println(choice_word[indexword_a]);

								indexword_a++;

								writer.println("<SEND> ");

								writer.println("<START>");

								acount = 0;

							}

							gamea = 0;

						} // for (printWritera)

							// input = in.readLine();

					} // if(gamea==1)



					/*

					 * gameb is 1 when the last person answer and next word show to first user, then

					 * gameb is 0.

					 */



					else if (gameb == 1) {

						for (PrintWriter writer : team_b.values()) /* send next word to first user. */

						{

							if (gameb == 1) {

								writer.println("<GIVEWORD>");

								writer.println(choice_word[indexword_b]);

								indexword_b++;

								writer.println("<SEND> ");

								writer.println("<START>");

								bcount = 0;

							}

							gameb = 0;



						} // for(printwriter b)



					} // if( gameb==1)



					input = in.readLine();

					Image k = null;



					/**

					 *  if last user answer, then client send Aanswer message to server. 

					 * then check the answer is correct the word.  update score.

					 * then send information the answer is wrong or correct to only last client.

					 * */

					if(input.length()>9) {

					if (input.substring(0, 9).equals("<Aanswer>"))

					{

						gamea = 1;

						String result = is_answer(input.substring(9), indexword_a - 1, "A");//check if the answer is correct then +1 if not +0

						out.println(result);//send to last client if the answer is right or wrong.



						for (PrintWriter writer : team_a.values()) /*update score to all user.*/

						{

							writer.println("<SCORE>");

							writer.println(score_a + ":" + score_b);

						}



					}



					/**

					 * same a A

					 * */

					if (input.substring(0, 9).equals("<Banswer>")) 

					{

						gameb = 1;

						String result = is_answer(input.substring(9), indexword_b - 1, "B");//check if the answer is correct then +1 if not +0

						out.println(result);//send to last client if the answer is right or wrong.

						

						for (PrintWriter writer : team_b.values()) /*update score to all user.*/

						{

							writer.println("<SCORE>");

							writer.println(score_b + ":" + score_a);



						}



					}

					}



					

					/** After user painting.

					 * first distinguish the user is team A and team B

					 * if the user sequence is current sequence(== sequence 1 or 2) 

					 *   : server read image file in first client painting file then store in "server_get.png" 

					 * if the user sequence +1 is current sequence(eg-first user then sequence is 2)

					 *   : server send image file to next user.

					 */

					

					  if (input.equals("<send>")) {

		                  int check = 0;

		                  int check2 = 0;

		                  int check3 = 0;

		                  int check4 = 0;

		                  // server get the picture from client.

		                  for (PrintWriter writer : team_a.values()) {

		                     if (name.startsWith("<A> ")) {



		                        Socket send = new Socket();//create new socket to use transfer file.

		                        FileInputStream fin2;

		                        

		                        /*if the game sequence is user sequence.

		                         * store each user painting in server "server_get.png"

		                         * */

		                        

		                        if (acount == incount_a) {

		                           while (check==0) {

		                              chattingcheck = 1;

		                              ServerSocket soc2 = new ServerSocket(11111);



		                              int count = 0;

		                              writer.println("<CANVAS>");

		                              send = soc2.accept();// if the clint ready for transfer file, then connection accept.

		                              

		                              InputStream in2 = null;

		                              FileOutputStream out2 = null;



		                              in2 = send.getInputStream();

		                              DataInputStream din = new DataInputStream(in2);



		                              int data = din.readInt();

		                              File file = new File("C:\\2-2\\server_get.png");

		                              out2 = new FileOutputStream(file);



		                              byte[] buffer = new byte[5000];

		                              int len;

		                              int temp;

		                              temp = data;

		                              

		                              for (; data > 0; data--) 

		                              {

		                                 len = in2.read(buffer);

		                                 out2.write(buffer, 0, len);

		                                 String queue = in.readLine();

		                                 if (Integer.toString(len).equals(queue)) {

		                                    count++;



		                                 } // if(len)

		                              } // for(데이터 읽어오는거)

		                              

		                              

		                              /*check the data sent by the client is same as the data received by the server. 

		                               * Until the data is same, repeatedly receive.

		                               * */

		                              if (count == temp) 

		                              {

		                                 //break;

		                            	  check = 1;

		                              } // if(다 왔는지 확인)

		                              

		                              out2.flush();

		                              out2.close();

		                              writer.println("<OUT>");

		                              soc2.close();



		                           } // while(check==0)

		                           chattingcheck = 0;



		                        } // if(ac=incount_a)



		                        

		                        /*

		                         *if the game sequence is user sequence.+1

		                         * send to each user(sequence 2 and 3) painting in server "server_get.png 

		                         * = send to previous user's painting to user.

		                         * 

		                         * */

		                        if (acount + 1 == incount_a) {



		                           chattingcheck = 1;

		                           

		                           // give picture to client

		                           while (check2 == 0) {

		                              chattingcheck = 1;

		                              ServerSocket soc = new ServerSocket(22222);



		                              int count = 0;

		                              writer.println("<RECEIVE>");

		                              send = soc.accept();

		                              OutputStream in3 = null;

		                              FileOutputStream out3 = null;



		                              in3 = send.getOutputStream();

		                              DataOutputStream din = new DataOutputStream(in3);

		                              DataInputStream dout1 = new DataInputStream(send.getInputStream());



		                              fin2 = new FileInputStream(new File("C:\\2-2\\server_get.png"));



		                              byte[] buffer2 = new byte[5000];

		                              int len;

		                              int data = 0;

		                              

		                              /*server open file server_get.png 

		                               * and read data and send it to user.

		                               * */

		                              

		                              while ((len = fin2.read(buffer2)) > 0) {

		                                 data++;

		                              } // while데이터

		                              din.writeInt(data);

		                              fin2.close();

		                              int temp;

		                              temp = data;

		                              fin2 = new FileInputStream(new File("C:\\2-2\\server_get.png"));



		                              /*send data (file) to client.

		                               * */

		                              for (; data > 0; data--) 

		                              {

		                                 len = fin2.read(buffer2);

		                                 in3.write(buffer2, 0, len);

		                                 writer.println(len);

		                              } 



		                              int ch1 = dout1.readInt();

		                              soc.close();

		                              if (ch1 == 1) 

		                              {

		                                 check2 = 1;

		                              } // if문



		                           } // while(check==0 인지 체크)



		                           

		                          

		                           //if the sequence is 3 (game sequence is 0) then user answer.

		                           if (incount_a == 2) 

		                           {

		                              writer.println("<AANSWERSHEET>");

		                           } 

		                           

		                           /*

		                            * 어케 설명????*/

		                           else 

		                           {

		                              writer.println("<SEND>");

		                              writer.println("<START>");



		                           }

		                           // else(AC==2) 아닐때

		                           acount++;

		                           break;

		                        } // if(Ac=incount_a+1)

		                        incount_a++;

		                     } // teamA 의 print Writer

		                  } // if A팀일때

		                  

		                  



		                  /** same as a team

		                   * After user painting.

		                   * first distinguish the user is team A and team B

		                   *  if the user sequence is current sequence(== sequence 1 or 2) 

		                   *     : server read image file in first client painting file then store in "server_get.png"

		                   *  if the user sequence +1 is current sequence(eg-first user then sequence is 2)

		                   *     : server send image file to next user.     

		                   */

		                  if (name.startsWith("<B> ")) {

							Socket send = new Socket();

							FileInputStream fin2;



							for (PrintWriter writer : team_b.values()) {

								if (bcount == incount_b) {



									while (check3 == 0) {

										chattingcheck = 1;

										ServerSocket soc = new ServerSocket(11111);



										int count = 0;

										writer.println("<CANVAS>");



										InputStream in2 = null;

										FileOutputStream out2 = null;



										in2 = send.getInputStream();

										DataInputStream din = new DataInputStream(in2);



										int data = din.readInt();

										File file = new File("C:\\2-2\\server_get.png");

										out2 = new FileOutputStream(file);



										byte[] buffer = new byte[5000];

										int len;

										int temp;

										temp = data;

										for (; data > 0; data--) {

											len = in2.read(buffer);

											out2.write(buffer, 0, len);

											String queue = in.readLine();

											if (Integer.toString(len).equals(queue)) {

												count++;



											} // if 데이터 길이 확인



										} // for문 데이터 읽기



										if (count == temp) {



											check3 = 1;

										} // if 다왔는지 확인



										out2.flush();

										out2.close();

										writer.println("<OUT>");

										soc.close();



									} // while(check==3)

									chattingcheck = 0;



								} // if(BC=incount_b)



								if (bcount + 1 == incount_b) {

									chattingcheck = 1;

									// give picture to client.



									while (check4 == 0) {

										chattingcheck = 1;

										ServerSocket soc = new ServerSocket(22222);



										int count = 0;

										writer.println("<RECEIVE>");



										send = soc.accept();

										OutputStream in3 = null;

										FileOutputStream out3 = null;



										in3 = send.getOutputStream();

										DataOutputStream din = new DataOutputStream(in3);

										DataInputStream dout1 = new DataInputStream(send.getInputStream());



										fin2 = new FileInputStream(new File("C:\\2-2\\server_get.png"));



										byte[] buffer2 = new byte[5000];

										int len;

										int data = 0;

										while ((len = fin2.read(buffer2)) > 0) {

											data++;

										} // while(데이터 읽기)

										din.writeInt(data);

										fin2.close();

										int temp;

										temp = data;



										fin2 = new FileInputStream(new File("C:\\2-2\\server_get.png"));



										for (; data > 0; data--) {

											len = fin2.read(buffer2);

											in3.write(buffer2, 0, len);

											writer.println(len);

										} // for문 데이터 보내기



										int ch1 = dout1.readInt();

										soc.close();

										if (ch1 == 1) {

											check4 = 1;

										} // if확인하는거



									} // while(check4==0) 확인

									if (incount_b == 2) {

										String useranswer;

										out.println("<BANSWERSHEET>");



									} // if(BC==1)- 마지막사람인지 확인

									else {

										writer.println("<SEND>");

										writer.println("<START>");

										bcount++;

										break;

									} // else(BC!=1)-마지막 사람 X



								} // if(BC==incount_b+1)확인



								incount_b++;

							} // for(B팀 writer)



						} // if(네임이 B일떄)

					} // if(클라이언트에ㅓ send보낼때)

					if (input == null) {



						return;

					}



					/*broadcast message to same team user. if the chat is accepted

					 * the game start chattingcheck set 1.

					 * */

					if (chattingcheck == 0) 

					{

						if (input.equals("<send>")) {

							continue;

						}

						/* braodcast message all user but same team! */

						for (HashMap.Entry<String, PrintWriter> entry : users.entrySet()) {

							if (name.startsWith("<A> ")) {

								for (PrintWriter writer : team_a.values()) {

									writer.println("MESSAGE " + name + ": " + input);

								} // for문 (printwriter-a)

								break;

							} // 이름 A팀으로 시작

							if (name.startsWith("<B> ")) {

								for (PrintWriter writer : team_b.values()) {
									
									writer.println("MESSAGE " + name + ": " + input);

								} // for(printwriter-b)

								break;

							} // if 이름 B팀으로 시작

						} // 큰 for문

					} // if(Chatting check==0 -채팅 허용)

				} // 완전 큰 while문!



			} // try

			catch (IOException e) {

				System.out.println(e);

			} finally {

				// This client is going down! Remove its name and its print



				// writer from the sets, and close its socket.

				if (name != null) {

					users.remove(name);
					

					if (name.startsWith("<A> ")) {
						
						team_a.remove(name);
						
						//out.println("<REMOVE>");
						
						ateamout--;

					} // if(이름 A팀)

					if (name.startsWith("<B> ")) {

						team_b.remove(name);

						bteamout--;

					} // if(이름 B팀)

				} // if(name!=null)

				if (out != null) {

					users.remove(out);

					/* The exit user info(name) broadcast all user. */

					for (PrintWriter writer : users.values()) {
						
						writer.println("MESSAGE " + "***" + name + "님이 퇴장하셨습니다. ***");
						out.println(name);
						
					} // for(Printwriter-퇴장소식)

				} // if(out!=null)



				try {

					socket.close();



				} catch (IOException e) {



				} // catch

			} // finally

		}// run 메소드

	}// handler class



	/*give category

	 * give category and order to all users. 

	 *  give word to first user and provide painting panel

	 */

	private static void give_category(String team) {

		if (team.equals("A")) {

			for (PrintWriter writer : team_a.values()) {

				int seq = asequence + 1;

				writer.println("<SEQUENCE>" + seq);// sequence

				writer.println("<GIVECATEGORY>");// category

				writer.println(category[choice_category]);// send category to client.

				if (asequence == 0) {

					writer.println("<GIVEWORD>");

					writer.println(choice_word[indexword_a]);// give word.

					indexword_a++;

					writer.println("<SEND> ");

					writer.println("<START>");

					chattingcheck = 1;// chat is forbidden.



				}

				asequence++;

			} // for(team a)

		} else if (team.equals("B")) {

			for (PrintWriter writer : team_b.values()) {

				writer.println("<SEQUENCE>" + bsequence + 1);

				writer.println("<GIVECATEGORY>");

				writer.println(category[choice_category]);



				if (bsequence == 0) {

					writer.println("<GIVEWORD>");

					writer.println(choice_word[indexword_b]);

					indexword_b++;// index is different because the game speeds can be different.

					writer.println("<SEND> ");

					writer.println("<START>");

					chattingcheck = 1;// chat is forbidden.

				} // if(bsequence==0)

				bsequence++;

			} // for(team_b)

		}



	}



	/* radnom_category

	 * the integer choice randomly(0~5)

	 * then by the integer category is decide.

	 * */

	private static void random_category() throws IOException 

	{

		read_file();

		Random random = new Random();

		choice_category = random.nextInt(6);// 0~5

		random_word();

	}



	/*random_word

	 * words in the selected category are arranged in random order.

	 * */

	private static void random_word() {



		for (int i = 0; i < word[choice_category].length; i++) {

			if (word[choice_category][i] == null)

				break;

			size_word++;

		}

		String temp[] = new String[size_word];

		int random[] = new int[size_word];

		Random r = new Random();

		for (int i = 0; i < size_word; i++) {

			random[i] = r.nextInt(size_word);

			for (int j = 0; j < i; j++) {

				if (random[i] == random[j])

					i--;

			}

		}

		for (int i = 0; i < size_word; i++) {

			choice_word[i] = word[choice_category][random[i]];



		}

	}



	/* is_answer

	 * parameter :answer, number== index, team="A" or "B"

	 * check the answer is correct.

	 * if answer is correct score+1 then return <"ANSWERCORRECT">(this send to last user)

	 * if not then return "<ANSWERWORNG>"+choice_word (this send to last user) 

	 */

	private static String is_answer(String answer, int number, String team)

	{

		if (answer.equalsIgnoreCase(choice_word[number])) {

			if (team.startsWith("A")) {

				score_a++;

			} else {

				score_b++;

			}

			return "<ANSWERCORRECT>";

		}

		return "<ANSWERWRONG>" + choice_word[number];

	}



	/*read file 

	 *(store category, word)

	 * then storing the infomation in array

	 * */

	private static void read_file() throws IOException {

		FileInputStream input = new FileInputStream("C:\\2-2\\category.txt");

		InputStreamReader reader = new InputStreamReader(input, "UTF-8");

		BufferedReader read = new BufferedReader(reader);



		for (int i = 0; i < 6; i++) {

			category[i] = read.readLine();

			String temp[] = read.readLine().split(",");



			for (int j = 0; j < temp.length; j++) {

				word[i][j] = temp[j];

			}

		}

	}

}