package lotto.controller;

import lotto.domain.lotto.Lottos;
import lotto.domain.winning.WinningStatistics;
import lotto.view.InputView;
import lotto.view.ResultView;

import java.io.PrintWriter;
import java.util.Scanner;

public class LottoController {
    private LottoController() {
    }

    public static void main(String[] args) {
        PrintWriter output = new PrintWriter(System.out, true);
        InputView inputView = new InputView(new Scanner(System.in), output);
        ResultView resultView = new ResultView(output);

        int money = inputView.getMoney();
        Lottos lottos = Lottos.withMoney(money);
        resultView.showLottos(lottos);
        WinningStatistics winningStatistics = lottos.getWinningStatistics(inputView.getLastLottoNumbers());
        resultView.showResult(winningStatistics, money);

        output.close();
    }
}