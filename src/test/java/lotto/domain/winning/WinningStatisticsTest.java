package lotto.domain.winning;

import lotto.domain.lotto.LottoNumberGenerator;
import lotto.domain.lotto.LottoRank;
import lotto.domain.lotto.LottoTicket;
import lotto.domain.lotto.Price;
import lotto.domain.number.LottoNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("WinningStatistics 클래스 테스트")
public class WinningStatisticsTest {
    String winningNumberString = "1,2,3,4,5,6";
    LottoNumber bonusLottoNumber = new LottoNumber(7);

    @DisplayName("WinningStatistics 객체를 생성할 수 있다.")
    @Test
    void create() {
        Price price = new Price(1000);
        LottoNumberGenerator lottoNumberGenerator = new LottoNumberGenerator(price, Collections.singletonList("1,2,3,4,5,6"));
        LottoTicket lottoTicket = new LottoTicket(lottoNumberGenerator.getLottoNumbers());

        Map<LottoRank, Long> lottoRankMap = lottoTicket.matchWinningNumber(new WinningLotto(winningNumberString), bonusLottoNumber);
        WinningStatistics winningStatistics = new WinningStatistics(price, lottoRankMap);

        Map<LottoRank, Integer> lottoRank = winningStatistics.getLottoRank();

        assertThat(lottoRank.get(LottoRank.FIRST)).isEqualTo(price.getLottoCount());
    }

    @DisplayName("WinningStatistics 객체 생성시 LottoRank MISS는 삭제한다.")
    @Test
    void create_remove_miss() {
        Price price = new Price(2000);
        LottoNumberGenerator lottoNumberGenerator = new LottoNumberGenerator(price, Collections.emptyList());
        LottoTicket lottoTicket = new LottoTicket(lottoNumberGenerator.getLottoNumbers());

        Map<LottoRank, Long> lottoRankMap = lottoTicket.matchWinningNumber(new WinningLotto(winningNumberString), bonusLottoNumber);
        WinningStatistics winningStatistics = new WinningStatistics(price, lottoRankMap);

        Map<LottoRank, Integer> lottoRank = winningStatistics.getLottoRank();

        assertThat(lottoRank.size()).isEqualTo(LottoRank.values().length - 1);
    }

    @DisplayName("수익률을 확인할 수 있다.")
    @Test
    void calculateProfit() {
        Price price = new Price(1000);
        LottoNumberGenerator lottoNumberGenerator = new LottoNumberGenerator(price, Collections.singletonList("1,2,3,4,5,6"));
        winningNumberString = "1,2,3,13,14,15";
        LottoTicket lottoTicket = new LottoTicket(lottoNumberGenerator.getLottoNumbers());

        Map<LottoRank, Long> lottoRankMap = lottoTicket.matchWinningNumber(new WinningLotto(winningNumberString), bonusLottoNumber);
        WinningStatistics winningStatistics = new WinningStatistics(price, lottoRankMap);

        BigDecimal profit = winningStatistics.calculateProfit();

        assertThat(profit).isEqualTo(new DecimalFormat("#,##0.00").format(LottoRank.FIFTH.getWinningMoney() / price.getPrice()));
    }
}