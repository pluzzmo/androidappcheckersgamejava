package id.ram;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckersActivity extends Activity {

	public ArrayAdapter<Integer> adapter;
	public Integer[] pieceValue; // -1 = uninitialized 0 = no piece 1 = Player 1's piece 2 = Player 2's piece 3 =  highlighted

	public boolean[] isKing;

	private GridView gv;
	private ImageView iv;

	public int position1 = 0;

	public int player1kills = 0;
	public int player2kills = 0;
	public boolean playerTurn;

	public boolean gameDone = true;

	public void createNewGame() {

		TextView tv = (TextView) findViewById(R.id.textView4);
		tv.setBackgroundColor(Color.BLACK);

		player1kills = 0;
		player2kills = 0;
		
		pieceValue = new Integer[64];

		isKing = new boolean[64];

		for (int i = 0; i<pieceValue.length; i++) {

			pieceValue[i] = -1;

			isKing[i] = false;

		}

		int j = 0;

		for (int i = 0; i<pieceValue.length-40; i++) {

			if ((j%2 == i%2) && (j != 3) && (j != 4)) {

				pieceValue[i] = 1;

			}

			if ((j%2 != i%2) && (j != 3) && (j != 4)) {

				pieceValue[i+40] = 2;

			}

			if ((i+1)%Math.sqrt(pieceValue.length) == 0) {
				j++;
			}

		}

		for (int i = 0; i<pieceValue.length; i++) {

			if (pieceValue[i] == -1) {
				pieceValue[i] = 0;
			}

		}

		playerTurn = true;

		gameDone = false;

	}

	public void switchTurns() {

		if (playerTurn == true) {
			playerTurn = false;
		} else if (playerTurn == false) {
			playerTurn = true;
		}

	}

	public void performMove(int pos1,int pos2,boolean turn) {

		TextView tv;

		if (Math.abs(pos2-pos1) > 10) {

			if (playerTurn == true) {

				player1kills++;

			} else if (playerTurn == false) {

				player2kills++;

			}

			if (pos2 > pos1) {

				if (pos2%8 < pos1%8) {

					pieceValue[pos1+7] = 0;

					tv = (TextView) gv.getChildAt(pos1+7);
					tv.setBackgroundColor(Color.TRANSPARENT);

				}

				if (pos2%8 > pos1%8) {

					pieceValue[pos1+9] = 0;

					tv = (TextView) gv.getChildAt(pos1+9);
					tv.setBackgroundColor(Color.TRANSPARENT);

				}

			}

			if (pos2 < pos1) {

				if (pos2%8 < pos1%8) {

					pieceValue[pos1-9] = 0;

					tv = (TextView) gv.getChildAt(pos1-9);
					tv.setBackgroundColor(Color.TRANSPARENT);

				}

				if (pos2%8 > pos1%8) {

					pieceValue[pos1-7] = 0;

					tv = (TextView) gv.getChildAt(pos1-7);
					tv.setBackgroundColor(Color.TRANSPARENT);

				}				

			}

		}

		pieceValue[pos2] = pieceValue[pos1];

		tv = (TextView) gv.getChildAt(pos2);

		if (pieceValue[pos2] == 1) {
			tv.setBackgroundResource(R.drawable.checkersblackpiece);
		} else if (pieceValue[pos2] == 2) {
			tv.setBackgroundResource(R.drawable.checkersredpiece);
		}

		pieceValue[pos1] = 0;
		tv = (TextView) gv.getChildAt(pos1);
		tv.setBackgroundColor(Color.TRANSPARENT);

		if (playerTurn == true) {

			if (pos2 > 55) {

				isKing[pos2] = true;

			}

		} else if (playerTurn == false) {

			if (pos2 < 8) {

				isKing[pos2] = true;

			}

		}

		if (turn) {
			switchTurns();
		}

	}

	public void clearArrayList(ArrayList a) {

		for (int i = 0; i < a.size(); i++) {
			a.remove(i);
		}

	}

	public void clearHighlightedPieceValues() {
		for (int i = 0; i < pieceValue.length; i++) {
			if (pieceValue[i] == 3) {
				gv.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
				pieceValue[i] = 0;
			}
		}
	}

	public void clearTextViews() {

		for (int i = 0; i < pieceValue.length; i++) {

			TextView tv = (TextView) gv.getChildAt(i);

			tv.setText(" ");
			tv.setBackgroundColor(Color.TRANSPARENT);

		}

	}

	public void highlightPossibleMoves(TextView tv, int position, int opp, boolean jump) {

		if (opp == 0) {

			if (pieceValue[position] == 1) {
				opp = 2;
			} else if (pieceValue[position] == 2) {
				opp = 1;
			}

		}

		// Player 1 \/
		// Player 2 /\

		// Checks for Empty

		if (isKing[position] == false) {

			if (jump == false) {

				if (opp == 2) {

					if (((position+1)%8 != 0) && (position <= 55)) {

						if ((pieceValue[position+9] == 0)) {	
							tv = (TextView) gv.getChildAt(position+9);
							tv.setBackgroundColor(Color.CYAN);		

							pieceValue[position+9] = 3;

						}

					}

					if ((position%8 != 0) && (position <= 55)) {

						if ((pieceValue[position+7] == 0)) {
							tv = (TextView) gv.getChildAt(position+7);
							tv.setBackgroundColor(Color.CYAN);		

							pieceValue[position+7] = 3;
						}

					}

				}

				if (opp == 1) {

					if ((position >= 8) && (position%8 != 0)) {
						if (pieceValue[position-9] == 0) {
							tv = (TextView) gv.getChildAt(position-9);
							tv.setBackgroundColor(Color.CYAN);		

							pieceValue[position-9] = 3;
						}
					}

					if (((position+1)%8 != 0) && (position >= 8)) {

						if ((pieceValue[position-7] == 0)) {
							tv = (TextView) gv.getChildAt(position-7);
							tv.setBackgroundColor(Color.CYAN);		

							pieceValue[position-7] = 3;
						}

					}

				}

			}

			// Checks for Jump-Over

			if (opp == 2) {

				if (((position+1)%8 != 0) && ((position+2)%8 != 0) && (position <= 47)) {

					if ((pieceValue[position+9] == opp)) {	

						if (pieceValue[position+18] == 0) {
							tv = (TextView) gv.getChildAt(position+18);
							tv.setBackgroundColor(Color.CYAN);		

							pieceValue[position+18] = 3;

							highlightPossibleMoves(tv, position+18,opp,true);

						}

					}

				}

				if (((position)%8 != 0) && ((position-1)%8 != 0) && (position <= 47)) {

					if ((pieceValue[position+7] == opp)) {	

						if (pieceValue[position+14] == 0) {
							tv = (TextView) gv.getChildAt(position+14);
							tv.setBackgroundColor(Color.CYAN);		

							pieceValue[position+14] = 3;

							highlightPossibleMoves(tv, position+14,opp,true);

						}

					}

				}

			}

			if (opp == 1) {

				if (((position+1)%8 != 0) && ((position+2)%8 != 0) && (position >= 16)) {

					if ((pieceValue[position-7] == opp)) {	

						if (pieceValue[position-14] == 0) {
							tv = (TextView) gv.getChildAt(position-14);
							tv.setBackgroundColor(Color.CYAN);		

							pieceValue[position-14] = 3;

							highlightPossibleMoves(tv, position-14,opp,true);

						}

					}

				}

				if (((position)%8 != 0) && ((position-1)%8 != 0) && (position >= 16)) {

					if ((pieceValue[position-9] == opp)) {	

						if (pieceValue[position+-18] == 0) {
							tv = (TextView) gv.getChildAt(position-18);
							tv.setBackgroundColor(Color.CYAN);		

							pieceValue[position-18] = 3;

							highlightPossibleMoves(tv, position-18,opp,true);

						}

					}

				}

			}

		} else if (isKing[position] == true) {

			// Checks for Empty

			if (jump == false) {

				if (((position+1)%8 != 0) && (position <= 55)) {

					if ((pieceValue[position+9] == 0)) {	
						tv = (TextView) gv.getChildAt(position+9);
						tv.setBackgroundColor(Color.CYAN);		

						pieceValue[position+9] = 3;

					}

				}

				if ((position%8 != 0) && (position <= 55)) {

					if ((pieceValue[position+7] == 0)) {
						tv = (TextView) gv.getChildAt(position+7);
						tv.setBackgroundColor(Color.CYAN);		

						pieceValue[position+7] = 3;

					}

				}

				if ((position >= 8) && (position%8 != 0)) {
					if (pieceValue[position-9] == 0) {
						tv = (TextView) gv.getChildAt(position-9);
						tv.setBackgroundColor(Color.CYAN);		

						pieceValue[position-9] = 3;

					}
				}

				if (((position+1)%8 != 0) && (position >= 8)) {

					if ((pieceValue[position-7] == 0)) {
						tv = (TextView) gv.getChildAt(position-7);
						tv.setBackgroundColor(Color.CYAN);		

						pieceValue[position-7] = 3;

					}

				}

			}

			// Checks for Jump-Over

			if (((position+1)%8 != 0) && ((position+2)%8 != 0) && (position <= 47)) {

				if ((pieceValue[position+9] == opp)) {	

					if (pieceValue[position+18] == 0) {
						tv = (TextView) gv.getChildAt(position+18);
						tv.setBackgroundColor(Color.CYAN);		

						pieceValue[position+18] = 3;

						highlightPossibleMoves(tv, position+18,opp,true);

					}

				}

			}

			if (((position)%8 != 0) && ((position-1)%8 != 0) && (position <= 47)) {

				if ((pieceValue[position+7] == opp)) {	

					if (pieceValue[position+14] == 0) {
						tv = (TextView) gv.getChildAt(position+14);
						tv.setBackgroundColor(Color.CYAN);		

						pieceValue[position+14] = 3;

						highlightPossibleMoves(tv, position+14,opp,true);

					}

				}

			}

			if (((position+1)%8 != 0) && ((position+2)%8 != 0) && (position >= 16)) {

				if ((pieceValue[position-7] == opp)) {	

					if (pieceValue[position-14] == 0) {
						tv = (TextView) gv.getChildAt(position-14);
						tv.setBackgroundColor(Color.CYAN);		

						pieceValue[position-14] = 3;

						highlightPossibleMoves(tv, position-14,opp,true);

					}

				}

			}

			if (((position)%8 != 0) && ((position-1)%8 != 0) && (position >= 16)) {

				if ((pieceValue[position-9] == opp)) {	

					if (pieceValue[position+-18] == 0) {
						tv = (TextView) gv.getChildAt(position-18);
						tv.setBackgroundColor(Color.CYAN);		

						pieceValue[position-18] = 3;

						highlightPossibleMoves(tv, position-18,opp,true);

					}

				}

			}

		}

	}

	public int isWin() {

		if (player1kills >= 12) {
			return 1;
		} else if (player2kills >= 12) {
			return 2;
		} else {
			return 0;
		}

	}

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setBackgroundColor(Color.WHITE);
		tv.setTextColor(Color.BLACK);

		tv = (TextView) findViewById(R.id.textView2);
		tv.setTextColor(Color.RED);

		gv = (GridView) findViewById(R.id.gridView1);
		gv.setBackgroundResource(R.drawable.checkersboard);

		if (gameDone) {
			createNewGame();
		}

		adapter = new ArrayAdapter<Integer>(this,R.layout.checkerstextview,pieceValue) {

			public View getView(int position, View convertView, ViewGroup parent) {				

				TextView tv;

				LayoutInflater inflater = (LayoutInflater)CheckersActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				if (convertView == null) {
					tv = (TextView) inflater.inflate(R.layout.checkerstextview, parent, false);
				} else {
					tv = (TextView) convertView;
				}

				if (pieceValue[position] == 1) {
					tv.setBackgroundResource(R.drawable.checkersblackpiece);
				} else if (pieceValue[position] == 2) {
					tv.setBackgroundResource(R.drawable.checkersredpiece);
				} else if (pieceValue[position] == 0) {
					tv.setBackgroundColor(Color.TRANSPARENT);
				} else if (pieceValue[position] == 3) {
					tv.setBackgroundColor(Color.CYAN);
				}

				return tv;
			}

		};

		gv.setAdapter(adapter);

		gv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView av, View v, int position, long id) {

				if (isWin() == 0) {

					TextView tv = (TextView) gv.getChildAt(position);

					if (pieceValue[position] != 0) {

						if ((pieceValue[position] == 1) && (playerTurn == true)) {

							clearHighlightedPieceValues();

							//highlight possible moves (set tv background as a highlight color) (set pieceValue[possible positions] = 3)

							highlightPossibleMoves(tv,position,0,false);

							position1 = position;

							adapter.notifyDataSetChanged();

						} else if ((pieceValue[position] == 2) && (playerTurn == false)) {

							clearHighlightedPieceValues();

							//highlight possible moves (set tv background as a highlight color) (set pieceValue[position] = 3)

							highlightPossibleMoves(tv,position,0,false);

							position1 = position;

							adapter.notifyDataSetChanged();

						}

					}		

					if (pieceValue[position] == 3) {

						for (int i = 0; i < pieceValue.length; i++) {

							if (pieceValue[i] == 3) {

								tv = (TextView) gv.getChildAt(i);
								tv.setBackgroundColor(Color.TRANSPARENT);

							}

							clearHighlightedPieceValues();

						}
							
						performMove(position1,position,true);
						
						adapter.notifyDataSetChanged();

						position1 = 0;

					}

				} else if (isWin() != 0) {

					TextView tv;

					gameDone = true;

					tv = (TextView) findViewById(R.id.textView3);

					if (isWin() == 1) {

						tv.setText("Player 1 Wins!");
						tv.setBackgroundColor(Color.WHITE);
						tv.setTextColor(Color.BLACK);

					} else if (isWin() == 2) {

						tv.setText("Player 2 Wins!");
						tv.setBackgroundColor(Color.WHITE);
						tv.setTextColor(Color.RED);

					}

					tv = (TextView) findViewById(R.id.textView4);
					tv.setBackgroundColor(Color.TRANSPARENT);

					Button b = (Button) findViewById(R.id.button1);

					b.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {

							createNewGame();
							
							adapter.notifyDataSetChanged();
							
							TextView t = (TextView) findViewById(R.id.textView4);
							
							t.setBackgroundColor(Color.BLACK);

						}

					}
					);

				}

			}

		}
		);

	}
}