package com.example.ksusha.bitsandpizzas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;

public class MainActivity extends Activity {

    private ShareActionProvider shareActionProvider;
    private ActionBarDrawerToggle drawerToggle;

    private DrawerLayout drawerLayout;

    private String[] titles;
    private ListView drawerList;

    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titles = getResources().getStringArray(R.array.titles);
        drawerList = (ListView) findViewById(R.id.drawer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Заполнить списковое представление на выдвижной панели и обеспечить его реакцию на щелчки.
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1, titles));

        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        //Вывести правильный текст
        if(savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("position");
            setActionBarTitle(currentPosition);
        } else {
            selectItem(0);
        }

        if (savedInstanceState == null) {
            selectItem(0);
        }

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.open_drawer, R.string.close_drawer) {
            //Вызывается при переходе выдвижной панели в полностью закрытое состояние.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            //Вызывается при переходе выдвижной панели в полностью открытое состояние.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

            @Override
            public void onBackStackChanged() {
                FragmentManager fragMan = getFragmentManager();
                Fragment fragment = fragMan.findFragmentByTag("visible_fragment");

                if (fragment instanceof TopFragment) {
                    currentPosition = 0;
                }
                if (fragment instanceof PizzaMaterialFragment) {
                    currentPosition = 1;
                }
                if (fragment instanceof PastaMaterialFragment) {
                    currentPosition = 2;
                }
                if (fragment instanceof StoresFragment) {
                    currentPosition = 3;
                }
                setActionBarTitle(currentPosition);
                drawerList.setItemChecked(currentPosition, true);
            }
        });
    }

    // Синхронизировать состояние ActionBarDrawerToggle с состоянием выдвижной панели.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Синхронизировать состояние
        drawerToggle.syncState();
    }


    // Для передачи информации о любых изменениях конфигурации ActionBarDrawerToggle.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    // Сохранить состояние currentPosition при уничтожении активности.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPosition);
    }

    // Отображать действие Share, если выдвижная панель закрыта; скрыть его, если выдвижная панель открыта.
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Если выдвижная панель открыта, скрыть элементы, связанные с контентом
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.action_share).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Заполнение меню; элементы (если они есть) добавляются на панель действий.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) menuItem.getActionProvider();
        setIntent("This is example text");

        return super.onCreateOptionsMenu(menu);
    }

    //Назначить действию Share интент для передачи информации.
    private void setIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        currentPosition = position;
        Fragment fragment;

        switch (position) {
            case 1:
                fragment = new PizzaMaterialFragment();
                break;
            case 2:
                fragment = new PastaMaterialFragment();
                break;
            case 3:
                fragment = new StoresFragment();
                break;
            default:
                fragment = new TopFragment();
        }

        // Отобразить фрагмент
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.content_frame, fragment, "visible_fragment");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        ft.commit();

        //Назначение заголовка панели действий
        setActionBarTitle(position);

        // закрываем выдвижную панель
        drawerLayout.closeDrawer(drawerList);

    }

    private void setActionBarTitle(int position) {
        String title;
        if (position == 0) {
            title = getResources().getString(R.string.app_name);
        } else {
            title = titles[position];
        }
        getActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Поручить обработку ActionBarDrawerToggle.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_create_order:
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
