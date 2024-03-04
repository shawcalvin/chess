package chess.ChessRuleBook;

import chess.*;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovementRule extends MovementRule {

    public BishopMovementRule() {
        this.pieceType = ChessPiece.PieceType.BISHOP;
    }
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new HashSet<>();
        int[][] moveDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 8; j++) {
                ChessPosition endPosition = new ChessPosition(position.getRow() + moveDirections[i][0] * j, position.getColumn() + moveDirections[i][1] * j);
                if (!super.positionIsOnBoard(endPosition)) {
                    break;
                }
                else if (super.positionIsEmpty(board, endPosition)) {
                    validMoves.add(new ChessMove(position, endPosition, null));
                }
                else if (super.positionIsCapturable(board, position, endPosition)) {
                    validMoves.add(new ChessMove(position, endPosition, null));
                    break;
                }
                else {
                    break;
                }
            }
        }

        return validMoves;
    }
}
