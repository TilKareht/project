/* My name is Til and this program is the Barcode Reader which uses classes, 
 * interfaces, and methods to create, read, display, and edit barcodes.
 */

public class Foothill
{
   public static void main(String[] args)
   {
      String[] sImageIn =
      {
         "* * * * * * * * * * * * * * *",
         "*                           *",
         "**********  *** *** *******  ",
         "* ***************************",
         "**    * *   * *  *   * *     ",
         "* **     ** **          **  *",
         "****** ****  **   *  ** ***  ",
         "****  **     *   *   * **   *",
         "***  *  *   *** * * ******** ",
         "*****************************"
      };      
            
         
      
      String[] sImageIn_2 =
      {
            "* * * * * * * * * * * * * * *",
            "*                           *",
            "*** ** ******** ** ***** *** ",
            "*  **** ***************** ***",
            "* *  *    *      *  *  *  *  ",
            "*       ** **** *          **",
            "*    * ****  **    * * * *** ",
            "***    ***       * **    * **",
            "*** *   **  *   ** * **   *  ",
            "*****************************"
            
      };
     
      BarcodeImage bc = new BarcodeImage(sImageIn);
      InfoBox dm = new InfoBox(bc);
     
      // First secret message
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
      
      // second secret message
      bc = new BarcodeImage(sImageIn_2);
      dm.scan(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
      
      // create your own message
      dm.readText("What a great resume builder this is!");
      dm.generateImageFromText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
   }   
}

// interface needed for later

interface BarcodeIO
{
   public boolean scan(BarcodeImage bc);
   public boolean readText(String text);
   public boolean generateImageFromText();
   public boolean translateImageToText();
   public void displayTextToConsole();
   public void displayImageToConsole();
}
class BarcodeImage implements Cloneable
{
   
   // sets variables needed for later
   
   public static final int MAX_HEIGHT = 30;    
   public static final int MAX_WIDTH = 65;
   public static final boolean FALSE_VALUE = false;
   public static final boolean TRUE_VALUE = true;
   public static final int MIN_VALUE = 0;
   public static final int ADJUSTMENT_VALUE = 1;
   public static final int ACTUAL_HEIGHT = 10;
   private boolean[][] imageData;
   
   // default constructor
   
   public BarcodeImage()
   {
      imageData = new boolean [MAX_HEIGHT][MAX_WIDTH];
      for (int row = 0; row < imageData.length; row ++ ) 
      {
         for (int column = 0; column < imageData[row].length; column ++ ) 
         {
            imageData[row][column] = FALSE_VALUE;
         } 
      }
   }
   
   // constructor that turn a array of string into a 2D array of booleans
   
   public BarcodeImage(String[] strData) 
   {   
      boolean checkSize = checkSize(strData);
      if (checkSize) 
      {
          imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
          int arrayLength = strData.length;
          int stringLength = strData[MIN_VALUE].length();
          char asterisk = '*';
          int imageDataLength = imageData.length;
          int differenceValue  = imageDataLength - ACTUAL_HEIGHT;
          
          // turns asterisk into true on the boolean array and space into false
          
          for (int row = 0; row < arrayLength; row ++ ) 
          {
             for (int index = 0; index < stringLength; index ++ ) 
             {
                 char addChar = strData[row].charAt(index);
                 if (addChar == asterisk) 
                 {
                     int rowValue = row + differenceValue;
                      setPixel(rowValue, index, TRUE_VALUE);
                 }
                 else
                 {
                    int rowValue = row + differenceValue;
                    setPixel(rowValue, index, FALSE_VALUE);
                 }
             }
          }
      }
   }
   
   // accessor for individual pixel in array
   
   public boolean getPixel(int row, int column)
   {
      boolean returnValue = FALSE_VALUE;
      int height = imageData.length - 1;
      int width = imageData[height].length;
      if (row <= height && column <= width)
      {
         returnValue = imageData[row][column];
      }
      return returnValue;
   }
   
   // mutator for individual pixel in array
   
   public boolean setPixel(int row, int column, boolean value)
   {
      int height = imageData.length - 1;
      int width = imageData[height].length;
      if (row <= height && column <= width && (value == FALSE_VALUE || 
            value == TRUE_VALUE))
      {
         imageData[row][column] = value;
         return TRUE_VALUE;
      }
      return FALSE_VALUE;
   }
   
   // helper method to check the size of the string array and if its null
   
   private boolean checkSize(String[] data)
   {
      String [] nullArray = null;
      if (data != nullArray)
      {
         int arrayLength = data.length;
         int stringLength = data[0].length();
         if (arrayLength <= MAX_HEIGHT && stringLength <= MAX_WIDTH)
         {
            return TRUE_VALUE;
         }
         return FALSE_VALUE;
      }
      else
      {
         return FALSE_VALUE;
      }
   }
   
   // clone method that clones imageData
   
   public Object clone() throws CloneNotSupportedException
   {
      BarcodeImage barcodeClone = (BarcodeImage) super.clone();
      
      // creates deep copy of imageData
      
      for (int row = 0; row < barcodeClone.imageData.length; row ++ ) 
      {
         for (int column = 0; column < barcodeClone.imageData[row].length; 
               column ++ ) 
         {
            boolean addItem = imageData[row][column];
            barcodeClone.imageData[row][column] = addItem;
         }
      }
      return barcodeClone;
   }
   
   // accessor for imageData
   
   public boolean[][] getImageData()
   {
      return imageData;
   }
}
class InfoBox implements BarcodeIO
{
   
   // sets variables needed for later
   
   public static final boolean TRUE_VALUE = true;
   public static final boolean FALSE_VALUE = false;
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';  
   public static final int DEFULAT_VALUE = 0;
   public static final int ACTUAL_HEIGHT = 10;
   public static final int MAX_LENGTH_TEXT = BarcodeImage.MAX_WIDTH;
   public static final int MIN_VALUE = 0;
   public static final String DEFUALT_TEXT = "undefined";
   public static final int ADJUSTMENT_VALUE = 1;
   private BarcodeImage image;
   private String text;
   private int actualWidth, actualHeight;
   
   // default constructor
   
   public InfoBox()
   {
      image = new BarcodeImage();
      text = DEFUALT_TEXT;
      actualWidth = 0;
      actualHeight = 0;
   }
   
   // constructor that takes in and changes only the image
   
   public InfoBox(BarcodeImage image)
   {
      text = DEFUALT_TEXT;
      actualWidth = 0;
      actualHeight = 0;;
      scan(image);
   }
   
   // constructor that takes in and changes only the text

   public InfoBox(String text)
   {
      image = new BarcodeImage();
      text = DEFUALT_TEXT;
      actualWidth = 0;
      actualHeight = 0;
      readText(text);
   }
   
   // mutator for image that uses clone and sets actualHeight and actualWidth
   
   public boolean scan(BarcodeImage image)
   {
      try
      {
         BarcodeImage clonedImage = (BarcodeImage)image.clone();
         this.image = clonedImage;
         actualHeight = computeSignalHeight();
         actualWidth = computeSignalWidth();
         return TRUE_VALUE;
      }
      catch(CloneNotSupportedException ex)
      {
         return FALSE_VALUE;
      }
   }
   
   // mutator for text
   
   public boolean readText(String text)
   {
      int length = text.length();
      if (length > MIN_VALUE && length <= MAX_LENGTH_TEXT)
      {
         this.text = text;
         return TRUE_VALUE;
      }
      return FALSE_VALUE;
   }
   
   // accessor for actualHeight
   
   public int getActualHeight()
   {
      return actualHeight;
   }
   
   // accessor for actualWidth
   
   public int getActualWidth()
   {
      return actualWidth;
   }
   
   // finds the actual width
   
   public int computeSignalWidth()
   {
      int width = 0;
      int imageLength = image.getImageData().length;
      int lastRow = imageLength - 1;
      int imageWidth = image.getImageData()[0].length;
      
      /* finds actualWidth based on how long the bottom closed line of  
         limitation is */
      
      for (int value = 0; value < imageWidth; value ++ ) 
      {
         boolean testBoolean = image.getPixel(lastRow, value);
         if (testBoolean == TRUE_VALUE)
         {
            width = width + 1;
         }
      }
      return width;
   }
   
   // finds the actual height
   
   private int computeSignalHeight()
   {
      int height = 0;
      int imageLength = image.getImageData().length;
      int firstColumn = 0;
      
      /* finds the actualHeight based on how long the left closed line of 
         limitation is */
      
      for (int value = 0; value < imageLength; value ++ ) 
      {
         boolean testBoolean = image.getPixel(value, firstColumn);
         if (testBoolean == TRUE_VALUE)
         {
            height ++ ;
         }
      }
      return height;
   }
   
   // generates an image from the text
   
   public boolean generateImageFromText()
   {
      int textLength = text.length();
      if (textLength > MIN_VALUE)
      {
         int imageDataLength = image.getImageData().length;
         
         /* converts each character to an array of booleans and adds it to the 
          * 2D array of booleans */
         
         
         for (int value = 0; value < textLength; value ++ ) 
         {
            char convertChar = text.charAt(value);
            boolean [] addArray = writeCharToColumn(convertChar);
            int differenceValue  = imageDataLength - addArray.length;
            int columnValue = value + ADJUSTMENT_VALUE;
            for (int row = 0; row < addArray.length; row ++ )
            {
               boolean addValue = addArray[row];
               int rowValue = row + differenceValue - ADJUSTMENT_VALUE;
               image.setPixel(rowValue, columnValue, addValue);
            }
         }
         addLimitLineAndOpenBorder();
         return TRUE_VALUE;
      }
      return FALSE_VALUE;
   }
   
   // creates an array of booleans that represents the char
   
   private boolean [] writeCharToColumn(char convertChar)
   {
      int [] minusValues = 
         {
               128, 64, 32, 16, 8, 4, 2, 1
         };
      boolean [] returnValue = new boolean[8];
      int intChar = (int)convertChar;
      
      // finds what values are true and false for the array
      
      for (int value = 0; value < minusValues.length; value ++ )
      {
         int testInt = intChar;
         testInt = testInt - minusValues[value];
         if (testInt >= MIN_VALUE)
         {
            returnValue [value] = TRUE_VALUE;
            intChar = testInt;
         }
         else
         {
            returnValue [value] = FALSE_VALUE;
         }
      }
      return returnValue;
   }
   
   // translates an image to the text
   
   public boolean translateImageToText()
   {
      boolean [][] nullArray = null;
      if (image.getImageData() == nullArray)
      {
         return FALSE_VALUE;
      }
      else
      {
         String translatedText = "";
         
         // converts each column into a character and adds it to the text string
         
         for (int column = 1; column < actualWidth; column ++ ) 
         {
            char addChar = readCharFromColumn(column);
            String addString = String.valueOf(addChar);
            translatedText = translatedText + addString;
         }
         text = translatedText;
         return TRUE_VALUE;
      }
   }
   
   // turns a column of booleans values into a char
   
   private char readCharFromColumn(int column)
   {
      int [] addValues = 
         {
               128, 64, 32, 16, 8, 4, 2, 1
         };
      int charValue = 0;
      int imageDataLength = image.getImageData().length;
      int differenceValue  = imageDataLength - actualHeight;
      int length = actualHeight - 1;
      
      // finds and adds all the true values to create the character
      
      for (int row = 1; row < length; row ++ )
      {
         int rowValue = row + differenceValue;
         boolean testValue = image.getPixel(rowValue, column);
         if (testValue == TRUE_VALUE)
         {
            int Index = row - 1;
            int addValue = addValues[Index];
            charValue = charValue + addValue;
         }
      }
      char keyChar = (char)charValue;
      return keyChar;
   }
   
   // adds the closed line of limitation and the open borderline
   
   private void addLimitLineAndOpenBorder()
   {
      
      // adds closed line of limitation
      
      int width = text.length();
      width ++ ;
      for (int value = 0; value < actualHeight; value ++ ) 
      {
         image.setPixel(value, MIN_VALUE, TRUE_VALUE);
      }
      int imageDataLength = image.getImageData().length;
      int differenceValue  = imageDataLength - actualHeight;
      for (int value = 0; value < width; value ++ ) 
      {
         int rowValue = actualHeight + differenceValue - ADJUSTMENT_VALUE;;
         image.setPixel(rowValue, value, TRUE_VALUE);
      }
      
      // adds open borderline
      
      for (int value = 0; value < width; value = value + 2 ) 
      {
         int rowValue = differenceValue;
         image.setPixel(rowValue,value , TRUE_VALUE);
         int columnValue = value + 1;
         image.setPixel(rowValue, columnValue, FALSE_VALUE);
      }
      actualWidth = computeSignalWidth();
   }
   
   // displays the text
   
   public void displayTextToConsole()
   {
      String addString = "The text is: ";
      System.out.println(addString + text);
   }
   
   // displays the image
   
   public void displayImageToConsole()
   {
      int printWidth = actualWidth + 2;
      String asteriks = "*";
      String space = " ";
      int imageDataLength = image.getImageData().length;
      int differenceValue  = imageDataLength - actualHeight;
      
      // prints top of border
      
      for (int i = 0; i < printWidth; i ++ ) 
      {
          System.out.print("-");
      }
      System.out.println();
      
      // displays outside border and the image as a barcode
      
      for(int row = 0; row < actualHeight; row ++ )
      {
         System.out.print("|");
         int rowValue = row + differenceValue;
         for(int column = 0; column < actualWidth; column ++ )
         {
            boolean testValue = image.getPixel(rowValue, column);
            if (testValue == true)
            {
               System.out.print(asteriks);
            }
            else
            {
               if (testValue == FALSE_VALUE)
               {
                  System.out.print(space);
               }
            }
         }
         System.out.println("|");
      }
   }
}