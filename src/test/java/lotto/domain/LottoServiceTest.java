package lotto.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LottoServiceTest {

    @ParameterizedTest
    @CsvSource(value = {"14000:14", "20000:20", "5500:5"}, delimiter = ':')
    @DisplayName("금액 입력받아 몇개 구매하는지 리턴한다.")
    void buyTicket(int amount, int result) {

        LottoService lottoService = new LottoService();

        assertThat(lottoService.buyTicket(amount)).isEqualTo(result);
    }

    @Test
    @DisplayName("구매 개수만큼 로또티켓들을 생성한다.")
    void createLottoTickets() {
        LottoService lottoService = new LottoService();
        List<Lotto> lottos = lottoService.createLottoTickets(5, Collections.emptyList());

        assertThat(lottos).hasSize(5);
    }

    @Test
    @DisplayName("당첨을 확인한다.")
    void findWinner() {
        LottoService lottoService = new LottoService();
        List<Lotto> lottos = Arrays.asList(
                new Lotto(Arrays.asList(1, 2, 3, 4, 5, 6)),
                new Lotto(Arrays.asList(1, 2, 3, 4, 5, 6)),
                new Lotto(Arrays.asList(3, 4, 5, 6, 7, 8)),
                new Lotto(Arrays.asList(4, 5, 6, 7, 8, 9)),
                new Lotto(Arrays.asList(5, 6, 7, 8, 9, 10)));

        Lotto lotto = new Lotto(Arrays.asList(1, 2, 3, 4, 5, 6));
        WinnerLotto winnerLotto = new WinnerLotto(lotto, 30);

        Map<WinnerType, Integer> winnerStat = lottoService.findWinnerStats(lottos, winnerLotto);

        assertThat(winnerStat).hasSize(3);
        assertThat(winnerStat.get(WinnerType.FIRST)).isEqualTo(2);
        assertThat(winnerStat.get(WinnerType.FOURTH)).isEqualTo(1);
        assertThat(winnerStat.get(WinnerType.FIFTH)).isEqualTo(1);
    }

    @Test
    @DisplayName("수익률을 구한다.")
    void findYield() {
        LottoService lottoService = new LottoService();
        Map<WinnerType, Integer> winnerStats = new HashMap<>();
        winnerStats.put(WinnerType.FOURTH, 2);
        winnerStats.put(WinnerType.FIFTH, 2);

        double yield = lottoService.findYield(winnerStats, 100);

        assertThat(yield).isEqualTo(1.1);
    }

    @Test
    @DisplayName("수익률은 소수 2자리 아래는 버림한다.")
    void findYieldFloor() {
        LottoService lottoService = new LottoService();
        Map<WinnerType, Integer> winnerStats = new HashMap<>();
        winnerStats.put(WinnerType.FIFTH, 1);

        double yield = lottoService.findYield(winnerStats, 14);

        assertThat(yield).isEqualTo(0.35);
    }

    @Test
    @DisplayName("보너스볼도 일치하는지 검사한다.")
    void checkBonusNumber() {
        LottoService lottoService = new LottoService();
        List<Lotto> lottos = Arrays.asList(new Lotto(Arrays.asList(1, 2, 3, 4, 5, 7)),
                new Lotto(Arrays.asList(1, 2, 3, 4, 5, 8)));

        Lotto lotto = new Lotto(Arrays.asList(1, 2, 3, 4, 5, 6));
        WinnerLotto winnerLotto = new WinnerLotto(lotto, 7);

        Map<WinnerType, Integer> winnerStats = lottoService.findWinnerStats(lottos, winnerLotto);

        assertThat(winnerStats).hasSize(2);
        assertThat(winnerStats.containsKey(WinnerType.SECOND)).isTrue();
        assertThat(winnerStats.containsKey(WinnerType.THIRD)).isTrue();
    }

    @Test
    @DisplayName("로또구매수보다 수동구매수가 클 수 없다.")
    void validateDuplicate() {

        List<Lotto> mannualLottos = Arrays.asList(new Lotto(Arrays.asList(1, 2, 3, 4, 5, 6)),
                new Lotto(Arrays.asList(1, 2, 3, 4, 5, 7)));
        LottoService lottoService = new LottoService();

        assertThatThrownBy(() -> {
            lottoService.createLottoTickets(1, mannualLottos);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("구매금액보다 수동으로 구매한 로또 수가 더 클 수 없습니다.");
    }
}