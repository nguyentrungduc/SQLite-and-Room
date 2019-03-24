# SQLite-and-Room

## DB in Android
- Khi phát triển ứng dụng Android, ta sẽ gặp những case phải lưu trữ dữ liệu ở đâu đó, Một db bạn tạo bạn tạo có thể rất tuyệt vời hoặc nó cũng làm việc phát triển tiếp theo của ứng dụng khó khăn hơn bh hết. Trong Android có 2 loại db phổ biến là Realm với SQLite

## Realm 
- Realm là một cơ sở dữ liệu nhẹ, có thể thay thế cả hai thư viện SQL và ORM trong các ứng dụng Android. Realm không sử dụng SQLite làm engine của nó. Thay vào đó, nó dùng core C++ nhằm mục đích cung cấp một thư viện cơ sở dữ liệu thay thế SQLite.
Realm lưu trữ dữ liệu trong các bảng viết bằng core C++. Việc này cho phép Realm được truy cập dữ liệu từ nhiều ngôn ngữ cũng như một loạt các truy vấn khác nhau.
- Dưới đây là những ưu điểm của Realm so với SQLite:
- Nhanh hơn so với SQLite (gấp 10 lần so với SQLite cho các hoạt động bình thường).
- Dễ sử dụng.
- Chuyển đổi đối tượng xử lý cho bạn.
- Thuận tiện cho việc tạo ra và lưu trữ dữ liệu nhanh chóng.
- Ngoài ra còn có một số nhược điểm sau :
- Vẫn còn đang phát triển.
- Không có nhiều kênh trao đổi trực tuyến.
- Không thể truy cập các đối tượng thông qua thread.

- Realm hỗ trợ đa nền tảng (hiện tại là Android, iOS, OSX), file CSDL có thể chia sẻ dễ dàng giữa các nền tảng trên. Realmluôn giữ tư tưởng nâng cao hiệu năng và giữ vững độ ổn định. Kết quả benchmark (có source code) cho thấy Realm nhanh hơn khoảng 2-10 lần trong các tác vụ đọc, ghi so với SQLitethuần và một số thư viện ORM phổ biến hiện nay.

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

###  Các thao tác cơ bản với db 

 - Thêm data : Truyền ContentValues object cho insert() method:
 	
		// Gets the data repository in write mode
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_NAME_TITLE, title);
		values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);

		// Insert the new row, returning the primary key value of the new row
		long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);

- Tham số thứ 2 : định nghĩa cách mà hệ thống sẽ thực hiện khi tham số values thứ 3 mà bạn thêm vào null. Trong trường hợp này, khi tham số thứ 2 để là null, thì hệ thống sẽ không thêm row mới khi value null. CÒn nếu tham số thứ 2 là tên cột thì hệ thống vẫn thêm row mới với giá trị thuộc tên cột đó là null.

- Method này trả về id của row mới, hoặc -1 nếu có lỗi xảy ra.

- Đọc data

		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    BaseColumns._ID,
		    FeedEntry.COLUMN_NAME_TITLE,
		    FeedEntry.COLUMN_NAME_SUBTITLE
		    };

		// Filter results WHERE "title" = 'My Title'
		String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
		String[] selectionArgs = { "My Title" };

		// How you want the results sorted in the resulting Cursor
		String sortOrder =
		    FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

		Cursor cursor = db.query(
		    FeedEntry.TABLE_NAME,   // The table to query
		    projection,             // The array of columns to return (pass null to get all)
		    selection,              // The columns for the WHERE clause
		    selectionArgs,          // The values for the WHERE clause
		    null,                   // don't group the rows
		    null,                   // don't filter by row groups
		    sortOrder               // The sort order
		    );

- Đocj kết quả : 

		List itemIds = new ArrayList<>();
		while(cursor.moveToNext()) {
		  long itemId = cursor.getLong(
		      cursor.getColumnIndexOrThrow(FeedEntry._ID));
		  itemIds.add(itemId);
		}
		cursor.close();
		
- Xóa data

		// Define 'where' part of query.
		String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
		// Specify arguments in placeholder order.
		String[] selectionArgs = { "MyTitle" };
		// Issue SQL statement.
		int deletedRows = db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs);

- Cập nhật Db

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		// New value for one column
		String title = "MyNewTitle";
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_NAME_TITLE, title);

		// Which row to update, based on the title
		String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
		String[] selectionArgs = { "MyOldTitle" };

		int count = db.update(
		    FeedReaderDbHelper.FeedEntry.TABLE_NAME,
		    values,
		    selection,
		    selectionArgs);


      

      

