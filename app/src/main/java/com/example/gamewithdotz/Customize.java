package com.example.gamewithdotz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class Customize extends AppCompatActivity {

    // track selected ImageButton per GridLayout
    private final Map<GridLayout, ImageButton> selectedButtonPerGrid = new HashMap<>();
    // track selected drawable id per GridLayout
    private final Map<GridLayout, Integer> selectedDrawablePerGrid = new HashMap<>();
    // track selected dot overlay per GridLayout
    private final Map<GridLayout, ImageView> selectedDotPerGrid = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customize);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cstm), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // populate grids after layout inflation
        populateAllGrids();
    }

    public void goBack(View v) {
        finish();
    }

    // ---------- GRID POPULATION ----------

    private void populateAllGrids() {
        GridLayout first  = findViewById(R.id.customGrid);
        GridLayout second = findViewById(R.id.greenGrid);
        GridLayout third  = findViewById(R.id.redGrid);
        GridLayout fourth = findViewById(R.id.blueGrid);

        int[] skinsFirst = new int[] {
                R.drawable.dot,
                R.drawable.dot_skin1,
                R.drawable.dot_skin2,
                R.drawable.dot_skin3,
                R.drawable.dot_skin4,
                R.drawable.dot_skin5// generic
        };

        int[] skinsGreen = new int[] {
                R.drawable.specialdot_green,       // primary green skin
                R.drawable.specialdot_green_skin1,
                R.drawable.specialdot_green_skin2,
                R.drawable.specialdot_green_skin3,
                R.drawable.specialdot_green_skin4,
                R.drawable.specialdot_green_skin5
        };

        int[] skinsRed = new int[] {
                R.drawable.specialdot_red,         // primary red skin
                R.drawable.specialdot_red_skin1,
                R.drawable.specialdot_red_skin2,
                R.drawable.specialdot_red_skin3,
                R.drawable.specialdot_red_skin4,
                R.drawable.specialdot_red_skin5
        };

        int[] skinsBlue = new int[] {
                R.drawable.specialdot_blue,        // primary blue skin
                R.drawable.specialdot_blue_skin1,
                R.drawable.specialdot_blue_skin2,
                R.drawable.specialdot_blue_skin3,
                R.drawable.specialdot_blue_skin4,
                R.drawable.specialdot_blue_skin5
        };

        populateGrid(first, skinsFirst);
        populateGrid(second, skinsGreen);
        populateGrid(third, skinsRed);
        populateGrid(fourth, skinsBlue);
    }

    /**
     * Populates the given grid with the provided skins array (one skin per cell).
     * If the skins array has fewer entries than cells, the last available skin is reused.
     *
     * @param grid GridLayout to populate
     * @param skinResIds array of drawable resource ids to assign to each cell
     */
    private void populateGrid(final GridLayout grid, final int[] skinResIds) {
        if (grid == null || skinResIds == null) return;

        grid.removeAllViews();

        final int cols = 3;
        final int rows = 2;
        grid.setColumnCount(cols);

        final int cellHeightDp = 100;
        final int marginDp = 4;

        final int indicatorSizeDp = 30; // size of the selection badge
        final int indicatorOffsetXDp = 0; // how far from the right edge (positive moves it inward)
        final int indicatorOffsetYDp = 0; // how far from the top edge (positive moves it downward)

        // SharedPreferences for this grid
        final SharedPreferences prefs = getSharedPreferences("customize_prefs", MODE_PRIVATE);

        // Use explicit keys per color/grid so you can access them later as requested:
        // savedBaseDot, savedGreenDot, savedRedDot, savedBlueDot
        final String prefKey;
        if (grid.getId() == R.id.customGrid) {
            prefKey = "savedBaseDot";
        } else if (grid.getId() == R.id.greenGrid) {
            prefKey = "savedGreenDot";
        } else if (grid.getId() == R.id.redGrid) {
            prefKey = "savedRedDot";
        } else if (grid.getId() == R.id.blueGrid) {
            prefKey = "savedBlueDot";
        } else {
            // fallback key (shouldn't be hit with your current layout)
            prefKey = "savedBaseDot";
        }

        final int savedSkin = prefs.getInt(prefKey, -1);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                final int cellIndex = r * cols + c;
                // choose the skin for this cell (if not enough skins provided, use last entry)
                final int skinRes = cellIndex < skinResIds.length ? skinResIds[cellIndex] : skinResIds[skinResIds.length - 1];

                // Container so we can overlay the dot at top-right
                final FrameLayout container = new FrameLayout(this);

                // ImageButton (fills container) — shows the skin
                final ImageButton btn = new ImageButton(this);
                btn.setImageResource(skinRes);
                btn.setBackgroundResource(R.drawable.custom_button);
                btn.setScaleType(android.widget.ImageView.ScaleType.CENTER_INSIDE);
                btn.setContentDescription("dot_cell_" + (cellIndex));

                FrameLayout.LayoutParams btnParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );
                btn.setLayoutParams(btnParams);

                // Dot ImageView (selection badge) — initially hidden
                final ImageView dot = new ImageView(this);
                // Use a small generic drawable as selection badge. Using R.drawable.selected (you have this)
                dot.setImageResource(R.drawable.selected);
                dot.setContentDescription("selection_dot_" + (cellIndex));
                dot.setVisibility(View.GONE);

                // dot layout params -> top-right with a small margin and explicit size
                FrameLayout.LayoutParams dotParams = new FrameLayout.LayoutParams(
                        dpToPx(this, indicatorSizeDp),
                        dpToPx(this, indicatorSizeDp),
                        Gravity.TOP | Gravity.END
                );
                // margin to bring the dot slightly inside the top-right corner
                dotParams.setMargins(0, dpToPx(this, indicatorOffsetYDp), dpToPx(this, indicatorOffsetXDp), 0);

                dot.setLayoutParams(dotParams);

                // Add button and dot to container
                container.addView(btn);
                container.addView(dot);

                // Give the container GridLayout layout params (weight for columns)
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams(
                        GridLayout.spec(r),
                        GridLayout.spec(c, 1f)
                );
                lp.width = 0; // allow weight to size columns equally
                lp.height = dpToPx(this, cellHeightDp);
                int m = dpToPx(this, marginDp);
                lp.setMargins(m, m, m, m);
                container.setLayoutParams(lp);

                // Decide if this cell should be selected initially:
                boolean shouldSelect = false;
                if (savedSkin != -1) {
                    // if a saved skin exists for this grid, select the matching cell
                    if (skinRes == savedSkin) {
                        shouldSelect = true;
                    }
                } else {
                    // no saved preference: make the first cell selected by default
                    if (cellIndex == 0) {
                        shouldSelect = true;
                    }
                }

                if (shouldSelect) {
                    dot.setVisibility(View.VISIBLE);
                    selectedButtonPerGrid.put(grid, btn);
                    selectedDotPerGrid.put(grid, dot);
                    selectedDrawablePerGrid.put(grid, skinRes);
                    // If no saved skin existed, save the default first selection under the specified key
                    if (savedSkin == -1) {
                        prefs.edit().putInt(prefKey, skinRes).apply();
                    }
                }

                // Click behavior: show dot for this cell, hide previous
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Hide previous dot and reset previous button if different
                        ImageView previousDot = selectedDotPerGrid.get(grid);
                        ImageButton previousBtn = selectedButtonPerGrid.get(grid);
                        if (previousDot != null && previousDot != dot) {
                            previousDot.setVisibility(View.GONE);
                        }
                        if (previousBtn != null && previousBtn != btn) {
                            previousBtn.setAlpha(1f);
                        }

                        // Show current dot and optionally change appearance of this button
                        dot.setVisibility(View.VISIBLE);
                        // you can dim the selected button if you like:
                        // btn.setAlpha(0.85f);
                        btn.setAlpha(1f);

                        // Save selection state
                        selectedButtonPerGrid.put(grid, btn);
                        selectedDotPerGrid.put(grid, dot);
                        selectedDrawablePerGrid.put(grid, skinRes);

                        // persist selection to SharedPreferences for this grid using requested keys
                        prefs.edit().putInt(prefKey, skinRes).apply();

                    }
                });

                // add container (not the button directly) to the GridLayout
                grid.addView(container);
            }
        }
    }

    // ---------- HELPERS ----------
    private int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
