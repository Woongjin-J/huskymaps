package huskymaps.rastering;

import huskymaps.graph.Coordinate;
import huskymaps.utils.Constants;

/**
 * @see Rasterer
 */
public class DefaultRasterer implements Rasterer {
    public TileGrid rasterizeMap(Coordinate ul, Coordinate lr, int depth) {
        double uy = Math.floor((Constants.ROOT_ULLAT - ul.lat()) / Constants.LAT_PER_TILE[depth]);
        double ux = Math.floor((ul.lon() - Constants.ROOT_ULLON) / Constants.LON_PER_TILE[depth]);
        double ly = Math.floor((Constants.ROOT_ULLAT - lr.lat()) / Constants.LAT_PER_TILE[depth]);
        double lx = Math.floor((lr.lon() - Constants.ROOT_ULLON) / Constants.LON_PER_TILE[depth]);

        if (uy > Constants.NUM_Y_TILES_AT_DEPTH[depth]) {
            Tile[][] grid = new Tile[1][1];
            grid[0][0] = new Tile(0, 0, 0);
            return new TileGrid(grid);
        }
        if (ux > Constants.NUM_X_TILES_AT_DEPTH[depth]) {
            Tile[][] grid = new Tile[1][1];
            grid[0][0] = new Tile(0, 0, 0);
            return new TileGrid(grid);
        }
        if (ly < 0) {
            Tile[][] grid = new Tile[1][1];
            grid[0][0] = new Tile(0, 0, 0);
            return new TileGrid(grid);
        }
        if (lx < 0) {
            Tile[][] grid = new Tile[1][1];
            grid[0][0] = new Tile(0, 0, 0);
            return new TileGrid(grid);
        }

        if (uy < 0) {
            uy = 0;
        }
        if (ux < 0) {
            ux = 0;
        }
        if (ly > Constants.NUM_Y_TILES_AT_DEPTH[depth] - 1) {
            ly = Constants.NUM_Y_TILES_AT_DEPTH[depth] - 1;
        }
        if (lx > Constants.NUM_X_TILES_AT_DEPTH[depth] - 1) {
            lx = Constants.NUM_X_TILES_AT_DEPTH[depth] - 1;
        }

        Tile[][] grid = new Tile[(int) Math.abs(ly - uy) + 1][(int) Math.abs(lx - ux) + 1];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = new Tile(depth, (int) ux + j, (int) uy + i);
            }
        }
        return new TileGrid(grid);
    }
}
