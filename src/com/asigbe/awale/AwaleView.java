package com.asigbe.awale;

import java.io.IOException;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Debug;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * This view manages the display of the game.
 * 
 * @author Delali Zigah
 */
public class AwaleView extends SurfaceView implements SurfaceHolder.Callback {
    class AwaleThread extends Thread implements Awale.AwaleListener {

	private static final int    SEED_HEIGHT    = 40;
	private static final int    SEED_WIDTH     = 40;

	private Typeface            typeFace;
	public boolean              run            = false;
	private final SurfaceHolder surfaceHolder;
	private int                 width;
	private int                 height;

	// creation of a listener

	private Bitmap              backgroundImage;
	private Bitmap              awaleImage;
	private Bitmap              oneSeedImage;
	private Bitmap              twoSeedsImage;
	private Bitmap              threeSeedsImage;
	private Bitmap              fourSeedsImage;
	private Bitmap              fiveSeedsImage;
	private Bitmap              sixSeedsImage;
	private Bitmap              sevenSeedsImage;
	private Bitmap              lotOfSeedsImage_1;
	private Bitmap              lotOfSeedsImage_2;
	private Bitmap              lotOfSeedsImage_3;

	// private Button seedsButton[][];
	private short               originRow      = -1;
	private short               originColumn   = -1;
	protected Bitmap            selectedBitmap = null;
	private Bitmap              pointSeedImage;
	private Bitmap              bitmaps[][];
	private Bitmap              stopImage;
	private int                 player1Color;
	private int                 player2Color;
	private Boolean             displayTurn    = new Boolean(false);
	private String              player1TurnString;
	private String              player2TurnString;
	private MediaPlayer         takeMediaPlayer;
	private MediaPlayer         dropMediaPlayer;

	public AwaleThread(SurfaceHolder surfaceHolder, Context context) {
	    this.takeMediaPlayer = MediaPlayer.create(context, R.raw.take);
	    this.dropMediaPlayer = MediaPlayer.create(context, R.raw.drop);
	    this.player1Color = getResources().getColor(R.color.player_1);
	    this.player2Color = getResources().getColor(R.color.player_2);
	    this.player1String = getContext().getString(R.string.player_1);
	    this.player2String = getContext().getString(R.string.player_2);
	    this.player1TurnString = getContext().getString(
		    R.string.player_1_turn);
	    this.player2TurnString = getContext().getString(
		    R.string.player_2_turn);
	    this.surfaceHolder = surfaceHolder;
	    this.bitmaps = new Bitmap[2][AwaleView.this.awale.size];
	    ((PlayActivity)context).awale.addListener(this);

	    Resources res = context.getResources();
	    this.backgroundImage = BitmapFactory.decodeResource(res,
		    R.drawable.motif);
	    this.awaleImage = BitmapFactory.decodeResource(res,
		    R.drawable.awale);
	    this.oneSeedImage = BitmapFactory.decodeResource(res,
		    R.drawable.seeds_1);
	    this.twoSeedsImage = BitmapFactory.decodeResource(res,
		    R.drawable.seeds_2);
	    this.threeSeedsImage = BitmapFactory.decodeResource(res,
		    R.drawable.seeds_3);
	    this.fourSeedsImage = BitmapFactory.decodeResource(res,
		    R.drawable.seeds_4);
	    this.fiveSeedsImage = BitmapFactory.decodeResource(res,
		    R.drawable.seeds_5);
	    this.sixSeedsImage = BitmapFactory.decodeResource(res,
		    R.drawable.seeds_6);
	    this.sevenSeedsImage = BitmapFactory.decodeResource(res,
		    R.drawable.seeds_7);
	    this.lotOfSeedsImage_1 = BitmapFactory.decodeResource(res,
		    R.drawable.seeds_lot_1);
	    this.lotOfSeedsImage_2 = BitmapFactory.decodeResource(res,
		    R.drawable.seeds_lot_2);
	    this.lotOfSeedsImage_3 = BitmapFactory.decodeResource(res,
		    R.drawable.seeds_lot_3);
	    this.stopImage = BitmapFactory.decodeResource(res, R.drawable.stop);
	    this.typeFace = Typeface.createFromAsset(context.getAssets(),
		    "fonts/african.ttf");
	    setOnTouchListener(new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
		    short row;
		    short column;
		    switch (event.getAction()) {
		    case MotionEvent.ACTION_DOWN:
			// test if the user has clicked on the tam tam to stop
			// the game
			if (hasClickedOnStopButton(event.getX(), event.getY())) {
			    ((PlayActivity) getContext()).gameOver();
			} else {
			    row = identifyRow(event.getY());
			    column = identifyColumn(event.getX());
			    // we verify that the selected case belongs to the
			    // current player and that it is not empty
			    if (row != -1
				    && column != -1
				    && AwaleView.this.awale.canPlayHere(row,
				            convertToViewColumn(row, column))) {
				AwaleThread.this.originRow = row;
				AwaleThread.this.originColumn = column;
				synchronized (AwaleThread.this.surfaceHolder) {
				    AwaleThread.this.selectedBitmap = getBitmap(
					    row, convertToViewColumn(row,
					            column));
				    if (AwaleThread.this.selectedBitmap != null) {
					AwaleThread.this.selectedBitmap = Bitmap
					        .createScaledBitmap(
					                AwaleThread.this.selectedBitmap,
					                AwaleThread.this.selectedBitmap
					                        .getWidth() + 7,
					                AwaleThread.this.selectedBitmap
					                        .getHeight() + 7,
					                true);
				    }
				}
			    }
			}
			break;
		    case MotionEvent.ACTION_UP:
			synchronized (AwaleThread.this.surfaceHolder) {
			    AwaleThread.this.selectedBitmap = null;
			}
			if (AwaleThread.this.originRow != -1
			        && AwaleThread.this.originColumn != -1) {
			    ((PlayActivity) getContext())
				    .play(AwaleThread.this.originRow,
				            convertToViewColumn(originRow,
				                    originColumn));
			}
			AwaleThread.this.originRow = -1;
			AwaleThread.this.originColumn = -1;

			break;
		    }
		    return true;
		}
	    });

	}

	private short convertToViewColumn(short side, short position) {
	    if (side == 0) {
		return (short) ((AwaleView.this.awale.size - 1) - position);
	    }
	    return position;
	}

	/**
	 * Identifies the matching column on the awale according to the x
	 * coordinate.
	 */
	private short identifyColumn(float x) {
	    int length = this.xCoordinates.length;
	    for (short i = 0; i < length; i++) {
		if (this.xOffset + this.xCoordinates[i] - SEED_WIDTH / 2 <= x
		        && x <= this.xOffset + this.xCoordinates[i]
		                + SEED_WIDTH / 2) {
		    return i;
		}
	    }
	    return -1;
	}

	/**
	 * Identifies the matching row on the awale according to the y
	 * coordinate.
	 */
	private short identifyRow(float y) {
	    int length = this.yCoordinates.length;
	    for (short i = 0; i < length; i++) {
		if (this.yOffset + this.yCoordinates[i] - SEED_HEIGHT / 2 <= y
		        && y <= this.yOffset + this.yCoordinates[i]
		                + SEED_HEIGHT / 2) {
		    return i;
		}
	    }
	    return -1;
	}

	@Override
	public void run() {
	    displayTurn();
	    while (this.run) {
		Canvas c = null;
		try {
		    c = this.surfaceHolder.lockCanvas(null);
		    synchronized (this.surfaceHolder) {
			if (AwaleView.this.isReadyForDrawing) {
			    doDraw(c);
			}
		    }
		} finally {
		    // do this in a finally so that if an exception is thrown
		    // during the above, we don't leave the Surface in an
		    // inconsistent state
		    if (c != null) {
			this.surfaceHolder.unlockCanvasAndPost(c);
		    }
		}
	    }
	}
	
	

	/**
	 * Used to signal the thread whether it should be running or not.
	 * Passing true allows the thread to run; passing false will shut it
	 * down if it's already running. Calling start() after this was most
	 * recently called with false will result in an immediate shutdown.
	 * 
	 * @param b
	 *            true to run, false to shut down
	 */
	public void setRunning(boolean b) {
	    this.run = b;
	}

	/* Callback invoked when the surface dimensions change. */
	public void setSurfaceSize(int width, int height) {
	    // synchronized to make sure these all change atomically
	    synchronized (this.surfaceHolder) {

		this.width = width;
		this.height = height;
		this.backgroundImage = Bitmap.createScaledBitmap(
		        this.backgroundImage, width, height, true);
		this.oneSeedImage = Bitmap.createScaledBitmap(
		        this.oneSeedImage, SEED_WIDTH, SEED_HEIGHT, true);
		this.twoSeedsImage = Bitmap.createScaledBitmap(
		        this.twoSeedsImage, SEED_WIDTH, SEED_HEIGHT, true);
		this.threeSeedsImage = Bitmap.createScaledBitmap(
		        this.threeSeedsImage, SEED_WIDTH, SEED_HEIGHT, true);
		this.fourSeedsImage = Bitmap.createScaledBitmap(
		        this.fourSeedsImage, SEED_WIDTH, SEED_HEIGHT, true);
		this.fiveSeedsImage = Bitmap.createScaledBitmap(
		        this.fiveSeedsImage, SEED_WIDTH, SEED_HEIGHT, true);
		this.sixSeedsImage = Bitmap.createScaledBitmap(
		        this.sixSeedsImage, SEED_WIDTH, SEED_HEIGHT, true);
		this.sevenSeedsImage = Bitmap.createScaledBitmap(
		        this.sevenSeedsImage, SEED_WIDTH, SEED_HEIGHT, true);
		this.lotOfSeedsImage_1 = Bitmap.createScaledBitmap(
		        this.lotOfSeedsImage_1, SEED_WIDTH, SEED_HEIGHT, true);
		this.lotOfSeedsImage_2 = Bitmap.createScaledBitmap(
		        this.lotOfSeedsImage_2, SEED_WIDTH, SEED_HEIGHT, true);
		this.lotOfSeedsImage_3 = Bitmap.createScaledBitmap(
		        this.lotOfSeedsImage_3, SEED_WIDTH, SEED_HEIGHT, true);
		this.pointSeedImage = Bitmap.createScaledBitmap(
		        this.oneSeedImage, 20, 20, true);
		this.stopImage = Bitmap.createScaledBitmap(this.stopImage, 50,
		        50, true);
		this.stopButtonX = this.width - this.stopImage.getWidth() - 5;
		this.stopButtonY = 5;

		buildBitmaps();
	    }
	}

	/**
	 * Handles a key-up event.
	 * 
	 * @param keyCode
	 *            the key that was pressed
	 * @param msg
	 *            the original event object
	 * @return true if the key was handled and consumed, or else false
	 */
	boolean doKeyUp(int keyCode, KeyEvent msg) {
	    boolean handled = false;

	    synchronized (this.surfaceHolder) {
	    }

	    return handled;
	}

	private final Paint                  paint          = new Paint(
	                                                            Paint.ANTI_ALIAS_FLAG);

	private final int                    xCoordinates[] = { 36, 100, 162,
	                                                            228, 294,
	                                                            362 };
	private final int                    yCoordinates[] = { 34, 103 };
	private int                          yOffset;
	private int                          xOffset;

	private final Paint                  selectedPaint  = new Paint();
	private final ColorMatrixColorFilter colorMatrix    = new ColorMatrixColorFilter(
	                                                            new float[] {
	                                                                    0,
	                                                                    0,
	                                                                    0,
	                                                                    0,
	                                                                    34,
	                                                                    0,
	                                                                    0,
	                                                                    0,
	                                                                    0,
	                                                                    177,
	                                                                    0,
	                                                                    0,
	                                                                    0,
	                                                                    0,
	                                                                    76,
	                                                                    0,
	                                                                    0,
	                                                                    0,
	                                                                    1,
	                                                                    0 // alpha
	                                                            // channel
	                                                            // is
	                                                            // preserved
	                                                            });
	private int                          stopButtonX;

	/**
	 * Draws the awale.
	 */
	private void doDraw(Canvas canvas) {

	    this.paint.setTypeface(this.typeFace);
	    this.yOffset = (this.height - this.awaleImage.getHeight()) / 2;
	    this.xOffset = (this.width - this.awaleImage.getWidth()) / 2;

	    // Draw the background image. Operations on the Canvas accumulate
	    // so this is like clearing the screen.
	    canvas.drawBitmap(this.backgroundImage, 0, 0, null);
	    canvas
		    .drawBitmap(this.awaleImage, this.xOffset, this.yOffset,
		            null);

	    int length = AwaleView.this.awale.size;

	    if (this.selectedBitmap != null && this.originColumn != -1
		    && this.originRow != -1) {
		this.selectedPaint.setColorFilter(this.colorMatrix);
		canvas.drawBitmap(this.selectedBitmap, this.xOffset
		        + this.xCoordinates[this.originColumn]
		        - this.selectedBitmap.getWidth() / 2, this.yOffset
		        + this.yCoordinates[this.originRow]
		        - this.selectedBitmap.getHeight() / 2,
		        this.selectedPaint);
	    }

	    this.paint.setTextSize(14);
	    short seedCount = 0;
	    Bitmap bitmap;
	    for (short i = 0; i < length; i++) {
		// draw seeds for the first player
		bitmap = this.bitmaps[0][i];
		if (bitmap != null) {
		    canvas.drawBitmap(bitmap, this.xOffset
			    + this.xCoordinates[i] - SEED_WIDTH / 2,
			    this.yOffset + this.yCoordinates[0] - SEED_HEIGHT
			            / 2, null);
		}
		// draw seeds count for the first player
		seedCount = AwaleView.this.awale.territory[0][(length - 1) - i];
		this.paint.setColor(this.player1Color);
		canvas.drawText(Short.toString(seedCount), this.xOffset
		        + this.xCoordinates[i] - 6, this.yOffset
		        + this.yCoordinates[0] - SEED_HEIGHT / 2 - 20,
		        this.paint);

		// draw seeds for the second player
		bitmap = this.bitmaps[1][i];
		if (bitmap != null) {
		    canvas.drawBitmap(bitmap, this.xOffset
			    + this.xCoordinates[i] - SEED_WIDTH / 2,
			    this.yOffset + this.yCoordinates[1] - SEED_HEIGHT
			            / 2, null);
		}

		// draw seeds count for the second player
		seedCount = AwaleView.this.awale.territory[1][i];
		this.paint.setColor(this.player2Color);
		canvas.drawText(Short.toString(seedCount), this.xOffset
		        + this.xCoordinates[i] - 6, this.yOffset
		        + this.yCoordinates[1] + SEED_HEIGHT / 2 + 30,
		        this.paint);
	    }

	    // draw first player points
	    this.paint.setColor(this.player1Color);
	    this.paint.setUnderlineText(AwaleView.this.awale.currentSide == 0);
	    canvas.drawText(this.player1String + " "
		    + AwaleView.this.awale.points[0], 5, 21, this.paint);
	    this.paint.setUnderlineText(false);
	    Rect rect = new Rect();
	    this.paint.getTextBounds(this.player1String, 0, this.player1String
		    .length(), rect);
	    int x = 0;
	    if (this.pointSeedImage != null) {
		for (int i = 0; i < AwaleView.this.awale.points[0]; i++) {
		    x = i * 7;
		    canvas.drawBitmap(this.pointSeedImage, x,
			    rect.height() + 15, null);
		}
	    }

	    // draw second player points
	    this.paint.setColor(this.player2Color);
	    this.paint.setUnderlineText(AwaleView.this.awale.currentSide == 1);
	    canvas.drawText(this.player2String + " "
		    + AwaleView.this.awale.points[1], 5, this.height - 15
		    - rect.height(), this.paint);
	    this.paint.setUnderlineText(false);
	    this.paint.getTextBounds(this.player2String, 0, this.player1String
		    .length(), rect);
	    x = 0;
	    if (this.pointSeedImage != null) {
		for (int i = 0; i < AwaleView.this.awale.points[1]; i++) {
		    x = i * 7;
		    canvas.drawBitmap(this.pointSeedImage, x, this.height - 20,
			    null);
		}
	    }
	    canvas.drawBitmap(this.stopImage, this.stopButtonX,
		    this.stopButtonY, null);

	    if (this.displayTurn) {
		String turnString = new String();
		if (AwaleView.this.awale.currentSide == 0) {
		    this.paint.setColor(this.player1Color);
		    turnString = this.player1TurnString;
		} else if (AwaleView.this.awale.currentSide == 1) {
		    this.paint.setColor(this.player2Color);
		    turnString = this.player2TurnString;
		}
		this.paint.setTextSize(32);
		this.paint.getTextBounds(turnString, 0, turnString.length(),
		        rect);
		canvas.drawText(turnString, this.width / 2 - rect.width() / 2,
		        this.height / 2 - 16, this.paint);
	    }
	    canvas.restore();
	}

	private final Random rand        = new Random(100);
	private int          stopButtonY = 25;
	private String       player1String;
	private String       player2String;

	private boolean hasClickedOnStopButton(float x, float y) {
	    if (x >= this.stopButtonX
		    && x <= this.stopButtonX + this.stopImage.getWidth()
		    && y >= this.stopButtonY
		    && y <= this.stopButtonY + this.stopImage.getHeight()) {
		return true;
	    }
	    return false;
	}

	private Bitmap getBitmap(short row, short column) {
	    short seedsCount = AwaleView.this.awale.territory[row][column];
	    Bitmap drawedBitmap = null;
	    switch (seedsCount) {
	    case 0:
		break;
	    case 1:
		drawedBitmap = this.oneSeedImage;
		break;
	    case 2:
		drawedBitmap = this.twoSeedsImage;
		break;
	    case 3:
		drawedBitmap = this.threeSeedsImage;
		break;
	    case 4:
		drawedBitmap = this.fourSeedsImage;
		break;
	    case 5:
		drawedBitmap = this.fiveSeedsImage;
		break;
	    case 6:
		drawedBitmap = this.sixSeedsImage;
		break;
	    case 7:
		drawedBitmap = this.sevenSeedsImage;
		break;
	    default:
		int index = rand.nextInt(3);
		switch (index) {
		case 0:
		    drawedBitmap = this.lotOfSeedsImage_1;
		    break;
		case 1:
		    drawedBitmap = this.lotOfSeedsImage_2;
		    break;
		default:
		    drawedBitmap = this.lotOfSeedsImage_3;
		    break;
		}
	    }
	    return drawedBitmap;
	}

	@Override
	public void awaleChanged(Awale awale, short eventType, short side,
	        short position) {
	    AwaleView.this.awale = awale;
	    switch (eventType) {
	    case Awale.POINTS:
		break;
	    case Awale.CURRENT_SIDE:
		displayTurn();
		break;
	    case Awale.TERRITORY:
		this.bitmaps[side][convertToViewColumn((short) side, position)] = getBitmap(
		        side, position);
		if (AwaleView.this.awale.territory[side][position] == 0) {
		    // we take seeds
		    if (this.takeMediaPlayer != null) {
			try {
	                    this.takeMediaPlayer.seekTo(0);
	                    this.takeMediaPlayer.start();
                        } catch (IllegalStateException e) {
                        }
		    }
		} else {
		    // we put seeds
		    if (this.dropMediaPlayer != null) {
			try {
	                    this.dropMediaPlayer.seekTo(0);
	                    this.dropMediaPlayer.start();
                        } catch (IllegalStateException e) {
                        }
		    }
		}
		break;
	    default:
		break;
	    }
	    postInvalidate();
	    if (eventType == Awale.TERRITORY) {
		try {
		    sleep(500);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}

	public void displayTurn() {
	    post(new Runnable() {
		@Override
		public void run() {
		    AwaleThread.this.displayTurn = true;
		}
	    });
	    postDelayed(new Runnable() {
		@Override
		public void run() {
		    AwaleThread.this.displayTurn = false;
		}
	    }, 1500);
	}

	private void buildBitmaps() {
	    for (short i = 0; i < AwaleView.this.awale.size; i++) {
		short convertToViewColumn = convertToViewColumn((short) 0, i);
		this.bitmaps[0][convertToViewColumn] = getBitmap((short) 0, i);
		convertToViewColumn = convertToViewColumn((short) 1, i);
		this.bitmaps[1][convertToViewColumn] = getBitmap((short) 1, i);
	    }
	}

	public void releasePlayers() {
	    if (this.dropMediaPlayer != null) {
		this.dropMediaPlayer.release();
	    }
	    
	    if (this.takeMediaPlayer != null) {
		this.takeMediaPlayer.release();
	    }
        }

    }

    /** The thread that actually draws the animation */
    private AwaleThread thread;
    /** current awale **/
    public Awale        awale;
    private boolean     isReadyForDrawing = false;

    /**
     * The constructor connects the callback of the thread to the view.
     */
    public AwaleView(Context context, AttributeSet attrs) {
	super(context, attrs);

	// register our interest in hearing about changes to our surface
	SurfaceHolder holder = getHolder();
	holder.addCallback(this);
	setFocusable(true); // make sure we get key events
    }

    /**
     * Fetches the animation thread corresponding to this LunarView.
     * 
     * @return the animation thread
     */
    public AwaleThread getThread() {
	return this.thread;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
	return this.thread.doKeyUp(keyCode, msg);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
	    int height) {
	this.isReadyForDrawing = true;
	this.thread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
	// start the thread here so that we don't busy-wait in run()
	// waiting for the surface to be created
	this.thread = new AwaleThread(holder, getContext());
	this.thread.setRunning(true);
	this.thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
	// we have to tell thread to shut down & wait for it to finish, or else
	// it might touch the Surface after we return and explode
	boolean retry = true;
	this.thread.setRunning(false);
	while (retry) {
	    try {
		this.thread.join();
		this.thread.releasePlayers();
		retry = false;
	    } catch (InterruptedException e) {
	    }
	}
    }

    /**
     * Sets the awale of the view.
     */
    public void setAwale(Awale awale) {
	this.awale = awale;
    }

}
