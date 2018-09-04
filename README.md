# GroupListView_SideBar

http://blog.csdn.net/williamchew/article/details/71247271
- ## 前言

    之前没有实现过这样的功能,最近刚好在做通讯方面的项目, 所以联系人这块需要一个分组和索引的功能, 今天我们来一起学习.
    
- ## 分组ListView

    实现的方法有两种:
    
    - 每一个ItemView都有一个Header, 通过数据俩控制其显示或隐藏
    
    - 使用不同类型的View.
    
    我这里用了一个巧妙的方法.
    
    #### 代码:
    
    1. #### Adapter
    
        可以看到这里实现了SectionIndexer接口, 这个接口用来控制ListView的分组的, 我们只要实现其中两个方法就好了, 分别是getPositionForSection(int position)和getSectionForPosition(int i).
        
        ```
        public class GroupListViewAdapter extends BaseAdapter implements SectionIndexer {
            private List<SortEntity> mEntities;
            private Context mContext;
        
            public GroupListViewAdapter(List<SortEntity> entities, Context context) {
                mEntities = entities;
                mContext = context;
            }
        
            @Override
            public int getCount() {
                return mEntities.size();
            }
        
            @Override
            public Object getItem(int i) {
                return mEntities.get(i);
            }
        
            @Override
            public long getItemId(int i) {
                return i;
            }
        
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                ViewHolder viewHolder = null;
                SortEntity sortEntity = mEntities.get(i);
                if (view == null) {
                    viewHolder = new ViewHolder();
                    view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_lv, null);
                    viewHolder.mTvLetter = (TextView) view.findViewById(R.id.tv_item_letter);
                    viewHolder.mTvTitle = (TextView) view.findViewById(R.id.tv_item_title);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                //下面就是来给字母分组
                int section = getSectionForPosition(i);
                if (i == getPositionForSection(section)) {
                    viewHolder.mTvLetter.setVisibility(View.VISIBLE);
                    viewHolder.mTvLetter.setText(sortEntity.getSortLetters());
                } else {
                    viewHolder.mTvLetter.setVisibility(View.GONE);
                }
                viewHolder.mTvTitle.setText(sortEntity.getName());
                return view;
            }
        
            class ViewHolder {
                TextView mTvTitle;
                TextView mTvLetter;
            }
        
            @Override
            public Object[] getSections() {
                return null;
            }
        
            //通过首字母的ascii码来获取在Listview中第一次出现该首字母的位置
            @Override
            public int getPositionForSection(int position) {
                for (int i = 0; i < getCount(); i++) {
                    String sortStr = mEntities.get(i).getSortLetters();
                    char firstChar = sortStr.toUpperCase().charAt(0);
                    if (firstChar == position) {
                        return i;
                    }
                }
                return -1;
            }
        
            //根据ListView的position来获取该位置的首字母的ASCII码值
            @Override
            public int getSectionForPosition(int i) {
                return mEntities.get(i).getSortLetters().charAt(0);
            }
        }
        ```
            
    2. SortEntity
        
        算是一个实体类, 实现了一个排序接口
        
        ```
        public class SortEntity implements Comparator<SortEntity> {
            private String mName;
            private String mSortLetters;
        
            public String getName() {
                return mName;
            }
        
            public void setName(String name) {
                mName = name;
            }
        
            public String getSortLetters() {
                return mSortLetters;
            }
        
            public void setSortLetters(String sortLetters) {
                mSortLetters = sortLetters;
            }
        
            @Override
            public int compare(SortEntity t1, SortEntity t2) {
                return t1.getSortLetters().compareTo(t2.getSortLetters());
            }
        }
        ```
        
- ## SideBar
    
    - 通过上面的ListView我们很方便的就可以实现这个索引栏
    
        自定义的View, 实现如下:
        
       ```
        public class SideBar extends View {
            //分类字母
            private static String[] sSideBarTitle;
            private int mChoose = -1;   //选择标记
            private Paint mPaint;       //画笔
            private TextView mTvDialog; //显示框
            private OnTouchLetterChangeListener mListener;  //回调接口
            int mHeight = 0;
            int mWidth = 0;
            int mSingleTextHeight;
        
            public SideBar(Context context) {
                super(context);
                initView();
            }
        
            public SideBar(Context context, @Nullable AttributeSet attrs) {
                super(context, attrs);
                initView();
            }
        
            public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                initView();
            }
        
            private void initView() {
                sSideBarTitle = getResources().getStringArray(R.array.sidebar);
                mPaint = new Paint();
                setPaint();
            }
        
            private void setPaint() {
                mPaint.reset();
                mPaint.setColor(Color.WHITE);
                mPaint.setAntiAlias(true);
                mPaint.setTypeface(Typeface.DEFAULT_BOLD);
                mPaint.setTextSize(30);
            }
        
        
            @Override
            protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                super.onSizeChanged(w, h, oldw, oldh);
                mHeight = getMeasuredHeight();
                mWidth = getMeasuredWidth();
                mSingleTextHeight = mHeight / sSideBarTitle.length;
            }
        
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                for (int i = 0; i < sSideBarTitle.length; i++) {
                    if (i == mChoose) {
                        mPaint.setColor(getResources().getColor(R.color.colorAccent));
                        mPaint.setFakeBoldText(true);
                    }
                    float xPos = mWidth / 2 - mPaint.measureText(sSideBarTitle[i]) / 2;
                    float yPos = mSingleTextHeight * i + mSingleTextHeight;
                    canvas.drawText(sSideBarTitle[i], xPos, yPos, mPaint);
                    setPaint();
                }
            }
        
            @Override
            public boolean dispatchTouchEvent(MotionEvent event) {
                int action = event.getAction();
                float y = event.getY();
                int lastChoose = mChoose;
                int scale = (int) (y / getHeight() * sSideBarTitle.length);
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        mChoose = -1;
                        if (mTvDialog != null) {
                            mTvDialog.setVisibility(View.INVISIBLE);
                        }
                        break;
                    default:
                        if (lastChoose != scale) {
                            if (scale >= 0 && scale < sSideBarTitle.length) {
                                if (mListener != null) {
                                    mListener.letterChange(sSideBarTitle[scale]);
                                }
                                if (mTvDialog != null) {
                                    mTvDialog.setText(sSideBarTitle[scale]);
                                    mTvDialog.setVisibility(View.VISIBLE);
                                }
                                mChoose = scale;
                            }
                        }
                        break;
                }
                invalidate();
                return true;
            }
        
        
            public void setOnTouchLetterChangeListener(OnTouchLetterChangeListener onTouchLetterChangeListener) {
                this.mListener = onTouchLetterChangeListener;
            }
        
            public interface OnTouchLetterChangeListener {
                void letterChange(String s);
            }
        
            public void setTvDialog(TextView tvDialog) {
                mTvDialog = tvDialog;
            }
        }
        ```
        
	---

    - MainActivity
        
        主要就是数据的处理, 和接口的回调
        
        ```
        public class MainActivity extends AppCompatActivity {

            private ListView mLvFriends;
            private Toolbar mTb;
            private List<SortEntity> mFriends;
            private Handler mHandler;
            private String mUserId;
            private GroupListViewAdapter mAdapter;
            private SortEntity mSortEntity;
            private SideBar mSideBar;
            private TextView mTvDialog;
        
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                initView();
                initEvent();
            }
            
            private void initView(){
                mLvFriends = (ListView) findViewById(R.id.lv_main);
                mSideBar = (SideBar) findViewById(R.id.sidebar);
                mTvDialog = (TextView) findViewById(R.id.tv_dialog);
                mSideBar.setTvDialog(mTvDialog);
                mSortEntity = new SortEntity();
                
            }
            
            private void initEvent(){
                mFriends = fillData(getResources().getStringArray(R.array.title));
                Collections.sort(mFriends, mSortEntity);    //对list排序
                mAdapter = new GroupListViewAdapter(mFriends, this);
                //接口的回调
                mSideBar.setOnTouchLetterChangeListener(new SideBar.OnTouchLetterChangeListener() {
                    @Override
                    public void letterChange(String s) {
                        int position = mAdapter.getPositionForSection(s.charAt(0));
                        if (position != -1) {
                            mLvFriends.setSelection(position);
                        }
                    }
                });
                mLvFriends.setAdapter(mAdapter);
            }
        
            //填充数据
            private List<SortEntity> fillData(String[] data) {
                List<SortEntity> entities = new ArrayList<>();
                for (int i = 0; i < data.length; i++) {
                    SortEntity sortEntity = new SortEntity();
                    sortEntity.setName(data[i]);
                    sortEntity.setSortLetters(data[i].substring(0, 1).toUpperCase());
                    entities.add(sortEntity);
                }
                return entities;
            }
        }
        ```
        
---

- ## 最终效果

	![这里写图片描述](http://img.blog.csdn.net/20170505235250250?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvd2lsbGlhbWNoZXc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
	
    项目地址: https://github.com/thatnight/GroupListView_SideBar

    
    
