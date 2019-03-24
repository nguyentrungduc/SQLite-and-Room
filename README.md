# SQLite-and-Room

## SQL 
- Lưu trữ dữ liệu vào database là ý tưởng dành cho các dữ liệu kiểu lặp lại hoặc có cấu trúc , ví dụ như thông tin danh bạ.
- APIs mà chúng ta sẽ dùng để sử dụng database trên Android có trong package sau : 
	Android.database.sqlite
-  Sẽ không có sự kiểm tra tại compile-time đối với các truy vẫn SQL. Khi cấu trúc data thay đổi bạn phải tự cập nhật các truy vấn này thủ công. 
- Bạn sẽ phải sử dụng nhiều code boilerplate để chuyển đổi giữa các SQL queries và data object
### Schema and contact
- Schema là một trong những nguyên tắc chính của db : nó là một khai báo về cách mà dữ liệu sẽ được tổ chức.

- Một contract class là container cho các hằng số sẽ định nghĩa tên cho URIs, tables, và column.Class này cho phép bạn sử dụng các thông tin trên trong tất cả các class thuộc cùng package.Và khi sử đổi chúng ta cũng chỉ cần thay đổi tại 1 nơi trong project.

- Cách tốt để tạo class này là sẽ đưa các định nghĩa global với db vào root-level của class.Sau đó ta sẽ tạo ra các iner-class tương ứng với các table cụ thể.

          public final class FeedReaderContract {
          // To prevent someone from accidentally instantiating the contract class,
          // make the constructor private.
          private FeedReaderContract() {}

          /* Inner class that defines the table contents */
          public static class FeedEntry implements BaseColumns {
              public static final String TABLE_NAME = "entry";
              public static final String COLUMN_NAME_TITLE = "title";
              public static final String COLUMN_NAME_SUBTITLE = "subtitle";
          }
      }
      
### Create DB
- Sau khi đã định nghĩa được db của bạn sẽ như thế nào, bây giờ chúng ta sẽ tạo ra db cùng các table của nó.
- Tạo statement để tạo hoặc xóa bảng :

          private static final String SQL_CREATE_ENTRIES =
          "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
          FeedEntry._ID + " INTEGER PRIMARY KEY," +
          FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
          FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";

      private static final String SQL_DELETE_ENTRIES =
          "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

- Sau đó dùng SQLiteOpenHelper để quản lý db.Khi dùng class này để tham chiếu đến db của bạn , hệ thống sẽ chỉ thực hiện những hoạt động mất thời gian với tạo và cập nhật db khi cần thiết, và không làm nó khi app khởi động.

- CHúng ta sẽ tạo subclass của nó và override  onCreate() và onUpgrade() callback methods. Bạn cũng có thể override  onDowngrade() hoặc  onOpen() methods, nhưng nó không phải là bắt buộc.

            public class FeedReaderDbHelper extends SQLiteOpenHelper {
                // If you change the database schema, you must increment the database version.
                public static final int DATABASE_VERSION = 1;
                public static final String DATABASE_NAME = "FeedReader.db";

                public FeedReaderDbHelper(Context context) {
                    super(context, DATABASE_NAME, null, DATABASE_VERSION);
                }
                public void onCreate(SQLiteDatabase db) {
                    db.execSQL(SQL_CREATE_ENTRIES);
                }
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    // This database is only a cache for online data, so its upgrade policy is
                    // to simply to discard the data and start over
                    db.execSQL(SQL_DELETE_ENTRIES);
                    onCreate(db);
                }
                public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    onUpgrade(db, oldVersion, newVersion);
                }
            }
   - Khởi tạo đối tượng helper để truy cập db : 
         
         FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getContext());

            

      

